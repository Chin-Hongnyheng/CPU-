package com.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.VPos;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

public class GanttChart {

    // Helper class
    static class ExecutionBlock {
        String label;
        int start;
        int end;

        ExecutionBlock(String label, int start, int end) {
            this.label = label;
            this.start = start;
            this.end = end;
        }
    }

    public static Pane buildFCFSGanttChart(int[] arrival, int[] burst, int n) {
        int[] completion = new int[n];
        int[] turnaround = new int[n];
        int[] waiting = new int[n];
        int[] response = new int[n];

        int currentTime = 0;
        ObservableList<Process> processList = FXCollections.observableArrayList();

        for (int i = 0; i < n; i++) {
            if (currentTime < arrival[i]) {
                currentTime = arrival[i];
            }
            currentTime += burst[i];
            completion[i] = currentTime;
            turnaround[i] = completion[i] - arrival[i];
            waiting[i] = turnaround[i] - burst[i];
            response[i] = waiting[i];

            processList.add(new Process(
                    "P" + (i + 1),
                    arrival[i],
                    burst[i],
                    completion[i],
                    turnaround[i],
                    waiting[i],
                    response[i]));
        }

        // Build Gantt chart
        Pane chartPane = new Pane();
        int scale = 20; // pixels per time unit
        int y = 50;
        int height = 50;

        int x = 0; // starting x position
        int fixedWidth = 50;

        for (Process p : processList) {
            Rectangle rect = new Rectangle(x, y, fixedWidth, height);
            rect.setFill(Color.LIGHTBLUE);
            rect.setStroke(Color.BLACK);

            Text label = new Text(p.getProcess());
            label.setStyle("-fx-font-weight: bold");

            // Center the label inside the rectangle
            label.setX(x + fixedWidth / 2.0 - label.getLayoutBounds().getWidth() / 2.0);
            label.setY(y + height / 2.0 + label.getLayoutBounds().getHeight() / 4.0);

            chartPane.getChildren().addAll(rect, label);
            x += fixedWidth;
        }

        int xTime = 0;
        Text zeroLabel = new Text(xTime, y + height + 20, "0");
        chartPane.getChildren().add(zeroLabel);

        for (Process p : processList) {
            xTime += fixedWidth;
            int time = p.getCompletionTime();
            Text timeLabel = new Text(xTime, y + height + 20, String.valueOf(time));
            chartPane.getChildren().add(timeLabel);
        }

        return chartPane;
    }

    public static Pane buildSJFGanttChart(int[] arrival, int[] burst, int n) {
        int[] completion = new int[n];
        int[] turnaround = new int[n];
        int[] waiting = new int[n];
        int[] response = new int[n];
        boolean[] done = new boolean[n];

        int currentTime = 0;
        int completed = 0;

        ObservableList<Process> processList = FXCollections.observableArrayList();

        while (completed < n) {
            int idx = -1;
            int minBurst = Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                if (!done[i] && arrival[i] <= currentTime && burst[i] < minBurst) {
                    minBurst = burst[i];
                    idx = i;
                }
            }

            if (idx == -1) {
                currentTime++;
                continue;
            }

            currentTime += burst[idx];
            completion[idx] = currentTime;
            turnaround[idx] = completion[idx] - arrival[idx];
            waiting[idx] = turnaround[idx] - burst[idx];
            response[idx] = waiting[idx];

            done[idx] = true;
            completed++;

            processList.add(new Process(
                    "P" + (idx + 1),
                    arrival[idx],
                    burst[idx],
                    completion[idx],
                    turnaround[idx],
                    waiting[idx],
                    response[idx]));
        }

        // Build Gantt chart
        Pane chartPane = new Pane();
        int y = 50;
        int height = 50;
        int fixedWidth = 50;
        int x = 0;

        for (Process p : processList) {
            Rectangle rect = new Rectangle(x, y, fixedWidth, height);
            rect.setFill(Color.LIGHTGREEN);
            rect.setStroke(Color.BLACK);

            Text label = new Text(p.getProcess());
            label.setStyle("-fx-font-weight: bold");

            label.setX(x + fixedWidth / 2.0 - label.getLayoutBounds().getWidth() / 2.0);
            label.setY(y + height / 2.0 + label.getLayoutBounds().getHeight() / 4.0);

            chartPane.getChildren().addAll(rect, label);
            x += fixedWidth;
        }

        int xTime = 0;
        Text zeroLabel = new Text(xTime, y + height + 20, "0");
        chartPane.getChildren().add(zeroLabel);

        for (Process p : processList) {
            xTime += fixedWidth;
            int time = p.getCompletionTime();
            Text timeLabel = new Text(xTime, y + height + 20, String.valueOf(time));
            chartPane.getChildren().add(timeLabel);
        }

        return chartPane;
    }

    public static Pane buildSRTGanttChart(int[] arrival, int[] burst, int n) {
        int[] remaining = burst.clone();
        int currentTime = 0;
        int completed = 0;
        List<ExecutionBlock> blocks = new ArrayList<>();

        int lastProcess = -1;

        // ---- SRT Scheduling Logic ----
        while (completed < n) {
            int idx = -1;
            int minRemaining = Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                if (arrival[i] <= currentTime && remaining[i] > 0) {
                    if (remaining[i] < minRemaining) {
                        minRemaining = remaining[i];
                        idx = i;
                    }
                }
            }

            if (idx == -1) {
                // CPU idle
                if (lastProcess != -2) {
                    blocks.add(new ExecutionBlock("Idle", currentTime, currentTime + 1));
                    lastProcess = -2;
                }
                currentTime++;
                continue;
            }

            // Add block for the process if different from last
            if (lastProcess != idx) {
                blocks.add(new ExecutionBlock("P" + (idx + 1), currentTime, currentTime + 1));
                lastProcess = idx;
            }

            remaining[idx]--;
            currentTime++;

            if (remaining[idx] == 0) {
                completed++;
            }
        }

        // ---- Draw chart ----
        Pane chartPane = new Pane();
        int blockSize = 50; // fixed width and height
        int y = 50;
        int height = 50;
        int xPos = 0;

        for (ExecutionBlock block : blocks) {
            // Each block is exactly 50x50
            Rectangle rect = new Rectangle(xPos, y, blockSize, height);
            rect.setFill(block.label.equals("Idle") ? Color.GRAY : Color.LIGHTGREEN);
            rect.setStroke(Color.BLACK);

            // Label centered
            Text label = new Text(block.label);
            label.setStyle("-fx-font-weight: bold");
            label.setBoundsType(TextBoundsType.VISUAL);
            label.setTextOrigin(VPos.CENTER);
            label.setX(xPos + (blockSize - label.getLayoutBounds().getWidth()) / 2.0);
            label.setY(y + height / 2.0);

            chartPane.getChildren().addAll(rect, label);

            xPos += blockSize; // next block
        }

        // Time markers
        xPos = 0;
        for (ExecutionBlock block : blocks) {
            Text start = new Text(xPos, y + height + 20, String.valueOf(block.start));
            chartPane.getChildren().add(start);
            xPos += blockSize;
        }

        // Add final end time
        if (!blocks.isEmpty()) {
            Text end = new Text(xPos, y + height + 20, String.valueOf(blocks.get(blocks.size() - 1).end));
            chartPane.getChildren().add(end);
        }

        return chartPane;
    }

    public static Pane buildRRGanttChart(int[] arrival, int[] burst, int n, int timeQuantum) {
        int[] remaining = burst.clone();
        int currentTime = 0;
        int completed = 0;

        Queue<Integer> queue = new LinkedList<>();
        boolean[] inQueue = new boolean[n];
        List<ExecutionBlock> blocks = new ArrayList<>();

        // Initially add processes that arrive at time 0
        for (int i = 0; i < n; i++) {
            if (arrival[i] <= currentTime) {
                queue.add(i);
                inQueue[i] = true;
            }
        }

        while (completed < n) {
            if (queue.isEmpty()) {
                blocks.add(new ExecutionBlock("Idle", currentTime, currentTime + 1));
                currentTime++;
                for (int i = 0; i < n; i++) {
                    if (arrival[i] <= currentTime && remaining[i] > 0 && !inQueue[i]) {
                        queue.add(i);
                        inQueue[i] = true;
                    }
                }
                continue;
            }

            int idx = queue.poll();
            inQueue[idx] = false;

            int execTime = Math.min(timeQuantum, remaining[idx]);
            blocks.add(new ExecutionBlock("P" + (idx + 1), currentTime, currentTime + execTime));

            remaining[idx] -= execTime;
            currentTime += execTime;

            for (int i = 0; i < n; i++) {
                if (arrival[i] > currentTime - execTime && arrival[i] <= currentTime && remaining[i] > 0
                        && !inQueue[i]) {
                    queue.add(i);
                    inQueue[i] = true;
                }
            }

            if (remaining[idx] > 0) {
                queue.add(idx);
                inQueue[idx] = true;
            } else {
                completed++;
            }
        }

        // Draw Gantt chart with fixed 50x50 blocks per execution segment
        Pane chartPane = new Pane();
        int blockSize = 50;
        int y = 50;
        int height = 50;

        for (int i = 0; i < blocks.size(); i++) {
            ExecutionBlock block = blocks.get(i);

            Rectangle rect = new Rectangle(i * blockSize, y, blockSize, height);
            rect.setFill(block.label.equals("Idle") ? Color.GRAY : Color.SKYBLUE);
            rect.setStroke(Color.BLACK);

            Text label = new Text(block.label);
            label.setStyle("-fx-font-weight: bold");
            label.setBoundsType(TextBoundsType.VISUAL);
            label.setTextOrigin(VPos.TOP);

            // Estimate center using layout bounds
            Bounds bounds = label.getLayoutBounds();
            double textWidth = bounds.getWidth();
            double textHeight = bounds.getHeight();

            label.setX(i * blockSize + (blockSize - textWidth) / 2.0);
            label.setY(y + (height - textHeight) / 2.0);

            chartPane.getChildren().addAll(rect, label);
        }

        // Time markers below each block using actual start times
        for (int i = 0; i < blocks.size(); i++) {
            ExecutionBlock block = blocks.get(i);
            Text timeStart = new Text(i * blockSize, y + height + 20, String.valueOf(block.start));
            chartPane.getChildren().add(timeStart);
        }

        // Final time marker using last block's end time
        if (!blocks.isEmpty()) {
            int finalTime = blocks.get(blocks.size() - 1).end;
            Text timeEnd = new Text(blocks.size() * blockSize, y + height + 20, String.valueOf(finalTime));
            chartPane.getChildren().add(timeEnd);
        }

        return chartPane;
    }

    public static Pane buildMLFQGanttChart(int[] arrival, int[] burst, int n, int tq1, int tq2) {
        int[] remaining = Arrays.copyOf(burst, n);
        int currentTime = 0;
        boolean[] started = new boolean[n];
        int completed = 0;

        Queue<Integer> q1 = new LinkedList<>();
        Queue<Integer> q2 = new LinkedList<>();
        List<ExecutionBlock> blocks = new ArrayList<>();

        // Initial load into Q1
        for (int i = 0; i < n; i++) {
            if (arrival[i] <= currentTime) {
                q1.add(i);
            }
        }

        while (completed < n) {
            // Load newly arrived processes into Q1
            for (int i = 0; i < n; i++) {
                if (arrival[i] <= currentTime && remaining[i] > 0 && !q1.contains(i) && !q2.contains(i)) {
                    q1.add(i);
                }
            }

            if (!q1.isEmpty()) {
                int i = q1.poll();
                if (!started[i])
                    started[i] = true;

                int exec = Math.min(tq1, remaining[i]);
                blocks.add(new ExecutionBlock("P" + (i + 1), currentTime, currentTime + exec));

                currentTime += exec;
                remaining[i] -= exec;

                if (remaining[i] > 0) {
                    q2.add(i);
                } else {
                    completed++;
                }

            } else if (!q2.isEmpty()) {
                int i = q2.poll();
                if (!started[i])
                    started[i] = true;

                int exec = Math.min(tq2, remaining[i]);
                blocks.add(new ExecutionBlock("P" + (i + 1), currentTime, currentTime + exec));

                currentTime += exec;
                remaining[i] -= exec;

                if (remaining[i] > 0) {
                    q2.add(i);
                } else {
                    completed++;
                }

            } else {
                blocks.add(new ExecutionBlock("Idle", currentTime, currentTime + 1));
                currentTime++;
            }
        }

        // Draw Gantt chart
        Pane chartPane = new Pane();
        int blockSize = 50;
        int y = 50;
        int height = 50;

        for (int i = 0; i < blocks.size(); i++) {
            ExecutionBlock block = blocks.get(i);

            Rectangle rect = new Rectangle(i * blockSize, y, blockSize, height);
            rect.setFill(block.label.equals("Idle") ? Color.GRAY : Color.SKYBLUE);
            rect.setStroke(Color.BLACK);

            Text label = new Text(block.label);
            label.setStyle("-fx-font-weight: bold");
            label.setBoundsType(TextBoundsType.VISUAL);
            label.setTextOrigin(VPos.TOP);

            chartPane.getChildren().add(rect); // ✅ Add rectangle first
            chartPane.getChildren().add(label); // ✅ Then add label

            label.applyCss(); // Ensure layout bounds are calculated

            double textWidth = label.getLayoutBounds().getWidth();
            double textHeight = label.getLayoutBounds().getHeight();

            label.setX(i * blockSize + (blockSize - textWidth) / 2.0);
            label.setY(y + (height - textHeight) / 2.0);
        }

        // Time markers
        for (int i = 0; i < blocks.size(); i++) {
            Text timeStart = new Text(i * blockSize, y + height + 20, String.valueOf(blocks.get(i).start));
            chartPane.getChildren().add(timeStart);
        }

        if (!blocks.isEmpty()) {
            int finalTime = blocks.get(blocks.size() - 1).end;
            Text timeEnd = new Text(blocks.size() * blockSize, y + height + 20, String.valueOf(finalTime));
            chartPane.getChildren().add(timeEnd);
        }

        return chartPane;
    }
}
