package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Controller {

    @FXML
    private ComboBox<String> CPUComboxBox;

    @FXML
    private Label arrival;

    @FXML
    private TextField arrivalTime;

    @FXML
    private Label burst;

    @FXML
    private TextField burstTime;

    @FXML
    private Button solve;

    @FXML
    private Button Nextbutton;

    @FXML
    private Label T1;

    @FXML
    private TextField T1Field;

    @FXML
    private Label T2;

    @FXML
    private TextField T2field;

    @FXML
    private Label T3;

    @FXML
    private ComboBox<String> T3CheckBox;

    @FXML
    private TableColumn<Process, String> process;

    @FXML
    private TableView<Process> processTable;

    @FXML
    private TableColumn<Process, Integer> timearrival;

    @FXML
    private TableColumn<Process, Integer> timeburst;

    @FXML
    private TableColumn<Process, Integer> timefinish;

    @FXML
    private TableColumn<Process, Integer> timeresponse;

    @FXML
    private TableColumn<Process, Integer> timeturnaround;

    @FXML
    private TableColumn<Process, Integer> timewait;

    @FXML
    private TextField RRvalue;

    @FXML
    private Label timeQRR;

    @FXML
    private Label CPUname;

    @FXML
    private Label chart;

    @FXML
    private Pane chartContainer;

    @FXML
    private Label displayAverage;

    @FXML
    private Label avgturnaround;

    @FXML
    private Label avgwait;

    @FXML
    public void initialize() {
        setItemInBox();
        setUpTable();
        SetUpBox();
        processTable.setPlaceholder(new Label(""));
    }

    public void setItemInBox() {

        ObservableList<String> algorithms = FXCollections.observableArrayList(
                "First Come First Serve, FCFS",
                "Shortest Job First, SJF (Non-Preemptive)",
                "Shortest Remaining Time, SRT (Preemptive)",
                "Round Robin, RR",
                "Multilevel Feedback Queue, MLFQ (3 Queues)");
        CPUComboxBox.setItems(algorithms);

        CPUComboxBox.setOnAction(event -> {
            Disable();
            String selected = CPUComboxBox.getValue();

            switch (selected) {
                case "First Come First Serve, FCFS":
                    EnableFCFS_SJF_SRT();
                    CPUname.setText("FCFS");
                    break;
                case "Shortest Job First, SJF (Non-Preemptive)":
                    EnableFCFS_SJF_SRT();
                    CPUname.setText("SJF");
                    break;
                case "Shortest Remaining Time, SRT (Preemptive)":
                    EnableFCFS_SJF_SRT();
                    CPUname.setText("SRT");
                    break;
                case "Round Robin, RR":
                    EnableRR();
                    CPUname.setText("RR");
                    break;
                case "Multilevel Feedback Queue, MLFQ (3 Queues)":
                    EnableMLFQ();
                    CPUname.setText("MLFQ");
                    break;
            }
        });
    }

    public void SetUpBox() {
        ObservableList<String> algorithms = FXCollections.observableArrayList(
                "First Come First Serve, FCFS");
        T3CheckBox.setItems(algorithms);
    }

    public void setUpTable() {
        process.setCellValueFactory(new PropertyValueFactory<>("process"));
        timearrival.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        timeburst.setCellValueFactory(new PropertyValueFactory<>("burstTime"));
        timefinish.setCellValueFactory(new PropertyValueFactory<>("completionTime"));
        timeturnaround.setCellValueFactory(new PropertyValueFactory<>("turnaroundTime"));
        timewait.setCellValueFactory(new PropertyValueFactory<>("waitingTime"));
        timeresponse.setCellValueFactory(new PropertyValueFactory<>("responseTime"));

        chart.setVisible(true);

        // Apply style to all columns
        TableColumn<Process, ?>[] columns = new TableColumn[] { process, timearrival, timeburst, timefinish,
                timeturnaround, timewait, timeresponse };

        for (TableColumn<Process, ?> col : columns) {
            col.setStyle(
                    "-fx-font-family: 'Times New Roman'; " +
                            "-fx-font-size: 16px; " +
                            "-fx-font-weight: bold; " +
                            "-fx-alignment: CENTER;");
        }
    }

    @FXML
    void Next(ActionEvent event) {
        if (!validateMLFQInputs()) {
            return;
        }
        Disable();
        EnableFCFS_SJF_SRT();
    }

    private boolean validateMLFQInputs() {
        String t1 = T1Field.getText().trim();
        if (!t1.matches("\\d+")) {
            System.out.println("Queue 1: Please enter ONE positive number only!");
            return false;
        }

        String t2 = T2field.getText().trim();
        if (!t2.matches("\\d+")) {
            System.out.println("Queue 2: Please enter ONE positive number only!");
            return false;
        }

        String t3 = T3CheckBox.getValue();
        if (t3 == null || t3.isEmpty()) {
            System.out.println("Queue 3: Please select a scheduling algorithm!");
            return false;
        }

        int q1TimeQuantum = Integer.parseInt(t1);
        int q2TimeQuantum = Integer.parseInt(t2);

        if (q2TimeQuantum <= q1TimeQuantum) {
            System.out.println("Queue 2: Time Quantum must be larger than Queue 1!");
            return false;
        }

        return true;
    }

    void EnableMLFQ() {
        T1.setVisible(true);
        T1Field.setVisible(true);
        T2.setVisible(true);
        T2field.setVisible(true);
        T3.setVisible(true);
        T3CheckBox.setVisible(true);
        Nextbutton.setVisible(true);
        T1.setManaged(true);
        T1Field.setManaged(true);
        T2.setManaged(true);
        T2field.setManaged(true);
        T3.setManaged(true);
        T3CheckBox.setManaged(true);
        Nextbutton.setManaged(true);
        CPUname.setVisible(true);
        CPUname.setManaged(true);
    }

    void Disable() {
        arrival.setVisible(false);
        arrivalTime.setVisible(false);
        burst.setVisible(false);
        burstTime.setVisible(false);
        solve.setVisible(false);
        arrival.setManaged(false);
        arrivalTime.setManaged(false);
        burst.setManaged(false);
        burstTime.setManaged(false);
        solve.setManaged(false);
        timeQRR.setManaged(false);
        timeQRR.setVisible(false);
        RRvalue.setManaged(false);
        RRvalue.setVisible(false);
        T1.setVisible(false);
        T1Field.setVisible(false);
        T2.setVisible(false);
        T2field.setVisible(false);
        T3.setVisible(false);
        T3CheckBox.setVisible(false);
        Nextbutton.setVisible(false);
        T1.setManaged(false);
        T1Field.setManaged(false);
        T2.setManaged(false);
        T2field.setManaged(false);
        T3.setManaged(false);
        T3CheckBox.setManaged(false);
        Nextbutton.setManaged(false);
    }

    void EnableFCFS_SJF_SRT() {
        arrival.setVisible(true);
        arrivalTime.setVisible(true);
        burst.setVisible(true);
        burstTime.setVisible(true);
        solve.setVisible(true);
        arrival.setManaged(true);
        arrivalTime.setManaged(true);
        burst.setManaged(true);
        burstTime.setManaged(true);
        solve.setManaged(true);
        CPUname.setVisible(true);
        CPUname.setManaged(true);
    }

    void EnableRR() {
        arrival.setVisible(true);
        arrivalTime.setVisible(true);
        burst.setVisible(true);
        burstTime.setVisible(true);
        solve.setVisible(true);
        arrival.setManaged(true);
        arrivalTime.setManaged(true);
        burst.setManaged(true);
        burstTime.setManaged(true);
        solve.setManaged(true);
        timeQRR.setManaged(true);
        timeQRR.setVisible(true);
        RRvalue.setManaged(true);
        RRvalue.setVisible(true);
        CPUname.setVisible(true);
        CPUname.setManaged(true);
    }

    @FXML
    void solveCPU(ActionEvent event) {
        String selected = CPUComboxBox.getValue();
        try {
            String[] arrivalTokens = arrivalTime.getText().trim().split("\\s+");
            String[] burstTokens = burstTime.getText().trim().split("\\s+");

            if (arrivalTokens.length != burstTokens.length) {
                throw new IllegalArgumentException("Arrival and Burst times must have the same length!");
            }

            int n = arrivalTokens.length;
            int[] arrival = new int[n];
            int[] burst = new int[n];
            for (int i = 0; i < n; i++) {
                // Check if contains comma → invalid input
                if (arrivalTokens[i].contains(",") || burstTokens[i].contains(",")) {
                    throw new IllegalArgumentException("Use spaces only, commas are not allowed!");
                }
                arrival[i] = Integer.parseInt(arrivalTokens[i]);
                burst[i] = Integer.parseInt(burstTokens[i]);
            }
            switch (selected) {
                case "First Come First Serve, FCFS":
                    calculateFCFS(arrival, burst, n);
                    processTable.setVisible(true);
                    break;
                case "Shortest Job First, SJF (Non-Preemptive)":
                    calculateSJF(arrival, burst, n);
                    processTable.setVisible(true);
                    break;
                case "Shortest Remaining Time, SRT (Preemptive)":
                    calculateSRT(arrival, burst, n);
                    processTable.setVisible(true);
                    break;
                case "Round Robin, RR":
                    calculateRR(arrival, burst, n);
                    processTable.setVisible(true);
                    break;
                case "Multilevel Feedback Queue, MLFQ (3 Queues)":
                    calculateMLFQ(arrival, burst, n);
                    processTable.setVisible(true);
                    break;
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    void calculateFCFS(int[] arrival, int[] burst, int n) {

        processTable.getItems().clear();

        int[] completion = new int[n];
        int[] turnaround = new int[n];
        int[] waiting = new int[n];
        int[] response = new int[n];

        int currentTime = 0;
        double totalTurnaround = 0;
        double totalWaiting = 0;

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

            totalTurnaround += turnaround[i];
            totalWaiting += waiting[i];

            processList.add(new Process(
                    "P" + (i + 1),
                    arrival[i],
                    burst[i],
                    completion[i],
                    turnaround[i],
                    waiting[i],
                    response[i]));
        }

        displayAverage.setVisible(true);

        double avgTurnaround = totalTurnaround / n;
        double avgWaiting = totalWaiting / n;

        avgturnaround.setText(String.format("Turnaround Time: %.2f", avgTurnaround));
        avgwait.setText(String.format("Waiting Time: %.2f", avgWaiting));

        processTable.setItems(processList);

        processTable.setFixedCellSize(50);
        processTable.prefHeightProperty().bind(
                processTable.fixedCellSizeProperty().multiply(processTable.getItems().size()).add(35));
        processTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        chart.setVisible(true);
        chart.setManaged(true);
        Pane ganttChart = GanttChart.buildFCFSGanttChart(arrival, burst, n);
        chartContainer.getChildren().clear();
        chartContainer.getChildren().add(ganttChart);
    }

    void calculateSJF(int[] arrival, int[] burst, int n) {
        processTable.getItems().clear();

        int[] completion = new int[n];
        int[] turnaround = new int[n];
        int[] waiting = new int[n];
        int[] response = new int[n];
        boolean[] done = new boolean[n];

        int currentTime = 0;
        int completed = 0;
        double totalTurnaround = 0;
        double totalWaiting = 0;

        ObservableList<Process> processList = FXCollections.observableArrayList();

        while (completed < n) {
            int idx = -1;
            int minBurst = Integer.MAX_VALUE;

            // Find the next process with shortest burst that has arrived
            for (int i = 0; i < n; i++) {
                if (!done[i] && arrival[i] <= currentTime && burst[i] < minBurst) {
                    minBurst = burst[i];
                    idx = i;
                }
            }

            if (idx == -1) {
                currentTime++; // no process has arrived yet, increment time
                continue;
            }

            currentTime += burst[idx];
            completion[idx] = currentTime;
            turnaround[idx] = completion[idx] - arrival[idx];
            waiting[idx] = turnaround[idx] - burst[idx];
            response[idx] = waiting[idx]; // same as waiting for non-preemptive SJF

            totalTurnaround += turnaround[idx];
            totalWaiting += waiting[idx];

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

        displayAverage.setVisible(true);

        double avgTurnaround = totalTurnaround / n;
        double avgWaiting = totalWaiting / n;

        avgturnaround.setText(String.format("Turnaround Time: %.2f", avgTurnaround));
        avgwait.setText(String.format("Waiting Time: %.2f", avgWaiting));

        processTable.setItems(processList);

        processTable.setFixedCellSize(50);
        processTable.prefHeightProperty().bind(
                processTable.fixedCellSizeProperty().multiply(processTable.getItems().size()).add(35));
        processTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        chart.setVisible(true);
        chart.setManaged(true);
        Pane ganttChart = GanttChart.buildSJFGanttChart(arrival, burst, n);
        chartContainer.getChildren().clear();
        chartContainer.getChildren().add(ganttChart);
    }

    void calculateSRT(int[] arrival, int[] burst, int n) {
        processTable.getItems().clear();

        int[] completion = new int[n];
        int[] turnaround = new int[n];
        int[] waiting = new int[n];
        int[] response = new int[n];
        boolean[] started = new boolean[n]; // to track first response

        int[] remaining = burst.clone(); // remaining burst times
        int completed = 0;
        int currentTime = 0;
        double totalTurnaround = 0;
        double totalWaiting = 0;

        ObservableList<Process> processList = FXCollections.observableArrayList();

        while (completed < n) {
            int idx = -1;
            int minRemaining = Integer.MAX_VALUE;

            // find process with shortest remaining time that has arrived
            for (int i = 0; i < n; i++) {
                if (arrival[i] <= currentTime && remaining[i] > 0 && remaining[i] < minRemaining) {
                    minRemaining = remaining[i];
                    idx = i;
                }
            }

            if (idx == -1) {
                currentTime++; // no process is ready
                continue;
            }

            // record first response time
            if (!started[idx]) {
                response[idx] = currentTime - arrival[idx];
                started[idx] = true;
            }

            remaining[idx]--; // execute 1 unit
            currentTime++;

            if (remaining[idx] == 0) {
                completed++;
                completion[idx] = currentTime;
                turnaround[idx] = completion[idx] - arrival[idx];
                waiting[idx] = turnaround[idx] - burst[idx];

                totalTurnaround += turnaround[idx];
                totalWaiting += waiting[idx];

                processList.add(new Process(
                        "P" + (idx + 1),
                        arrival[idx],
                        burst[idx],
                        completion[idx],
                        turnaround[idx],
                        waiting[idx],
                        response[idx]));
            }
        }

        displayAverage.setVisible(true);

        double avgTurnaround = totalTurnaround / n;
        double avgWaiting = totalWaiting / n;

        avgturnaround.setText(String.format("Turnaround Time: %.2f", avgTurnaround));
        avgwait.setText(String.format("Waiting Time: %.2f", avgWaiting));

        processTable.setItems(processList);

        processTable.setFixedCellSize(50);
        processTable.prefHeightProperty().bind(
                processTable.fixedCellSizeProperty().multiply(processTable.getItems().size()).add(35));
        processTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        chart.setVisible(true);
        chart.setManaged(true);
        Pane ganttChart = GanttChart.buildSRTGanttChart(arrival, burst, n);
        chartContainer.getChildren().clear();
        chartContainer.getChildren().add(ganttChart);
    }

    void calculateRR(int[] arrival, int[] burst, int n) {
        processTable.getItems().clear();

        // Get time quantum from TextField
        String input = RRvalue.getText().trim();
        String[] tokens = input.split("\\s+");

        if (tokens.length != 1) {
            timeQRR.setText("Please enter only 1 time quantum!");
            return;
        }

        int timeQuantum;
        try {
            timeQuantum = Integer.parseInt(tokens[0]);
            if (timeQuantum <= 0)
                throw new NumberFormatException();
        } catch (NumberFormatException e) {
            timeQRR.setText("Invalid time quantum!");
            return;
        }

        int[] completion = new int[n];
        int[] turnaround = new int[n];
        int[] waiting = new int[n];
        int[] response = new int[n];
        boolean[] started = new boolean[n];
        int[] remaining = burst.clone();

        int currentTime = 0;
        int completed = 0;
        double totalTurnaround = 0;
        double totalWaiting = 0;

        Queue<Integer> queue = new LinkedList<>();
        boolean[] inQueue = new boolean[n];

        for (int i = 0; i < n; i++) {
            if (arrival[i] <= currentTime) {
                queue.add(i);
                inQueue[i] = true;
            }
        }

        while (completed < n) {
            if (queue.isEmpty()) {
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

            if (!started[idx]) {
                response[idx] = currentTime - arrival[idx];
                started[idx] = true;
            }

            int execTime = Math.min(timeQuantum, remaining[idx]);
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
                completion[idx] = currentTime;
                turnaround[idx] = completion[idx] - arrival[idx];
                waiting[idx] = turnaround[idx] - burst[idx];

                totalTurnaround += turnaround[idx];
                totalWaiting += waiting[idx];
            }
        }

        ObservableList<Process> processList = FXCollections.observableArrayList();
        for (int i = 0; i < n; i++) {
            processList.add(new Process(
                    "P" + (i + 1),
                    arrival[i],
                    burst[i],
                    completion[i],
                    turnaround[i],
                    waiting[i],
                    response[i]));
        }

        displayAverage.setVisible(true);
        avgturnaround.setText(String.format("Turnaround Time: %.2f", totalTurnaround / n));
        avgwait.setText(String.format("Waiting Time: %.2f", totalWaiting / n));

        processTable.setItems(processList);
        processTable.setFixedCellSize(50);
        processTable.prefHeightProperty().bind(
                processTable.fixedCellSizeProperty().multiply(processTable.getItems().size()).add(35));
        processTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        chart.setVisible(true);
        chart.setManaged(true);

        Pane rrChart = GanttChart.buildRRGanttChart(arrival, burst, n, timeQuantum);
        chartContainer.getChildren().clear();
        chartContainer.getChildren().add(rrChart);
    }

    void calculateMLFQ(int[] arrival, int[] burst, int n) {
        processTable.getItems().clear();

        int tq1, tq2;
        try {
            tq1 = Integer.parseInt(T1Field.getText());
            tq2 = Integer.parseInt(T2field.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter valid integers for T1 and T2!");
            alert.show();
            return;
        }

        int[] remaining = Arrays.copyOf(burst, n);
        int[] completion = new int[n];
        int[] turnaround = new int[n];
        int[] waiting = new int[n];
        int[] response = new int[n];
        boolean[] started = new boolean[n];

        double totalTurnaround = 0;
        double totalWaiting = 0;

        int currentTime = 0;
        Queue<Integer> q1 = new LinkedList<>();
        Queue<Integer> q2 = new LinkedList<>();

        for (int i = 0; i < n; i++) {
            if (arrival[i] <= currentTime) {
                q1.add(i);
            }
        }

        ObservableList<Process> processList = FXCollections.observableArrayList();

        while (!q1.isEmpty() || !q2.isEmpty() || Arrays.stream(remaining).anyMatch(r -> r > 0)) {
            for (int i = 0; i < n; i++) {
                if (arrival[i] <= currentTime && remaining[i] > 0 && !q1.contains(i) && !q2.contains(i)) {
                    q1.add(i);
                }
            }

            if (!q1.isEmpty()) {
                int i = q1.poll();
                if (!started[i]) {
                    response[i] = currentTime - arrival[i];
                    started[i] = true;
                }
                int exec = Math.min(tq1, remaining[i]);
                currentTime += exec;
                remaining[i] -= exec;

                if (remaining[i] > 0) {
                    q2.add(i);
                } else {
                    completion[i] = currentTime;
                }

            } else if (!q2.isEmpty()) {
                int i = q2.poll();
                if (!started[i]) {
                    response[i] = currentTime - arrival[i];
                    started[i] = true;
                }
                int exec = Math.min(tq2, remaining[i]);
                currentTime += exec;
                remaining[i] -= exec;

                if (remaining[i] > 0) {
                    q2.add(i);
                } else {
                    completion[i] = currentTime;
                }

            } else {
                currentTime++;
            }
        }

        for (int i = 0; i < n; i++) {
            turnaround[i] = completion[i] - arrival[i];
            waiting[i] = turnaround[i] - burst[i];

            totalTurnaround += turnaround[i];
            totalWaiting += waiting[i];

            processList.add(new Process(
                    "P" + (i + 1),
                    arrival[i],
                    burst[i],
                    completion[i],
                    turnaround[i],
                    waiting[i],
                    response[i]));
        }

        displayAverage.setVisible(true);
        avgturnaround.setText(String.format("Turnaround Time: %.2f", totalTurnaround / n));
        avgwait.setText(String.format("Waiting Time: %.2f", totalWaiting / n));

        processTable.setItems(processList);
        processTable.setFixedCellSize(50);
        processTable.prefHeightProperty().bind(
                processTable.fixedCellSizeProperty().multiply(processTable.getItems().size()).add(35));
        processTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        chart.setVisible(true);
        chart.setManaged(true);

        Pane ganttChart = GanttChart.buildMLFQGanttChart(arrival, burst, n, tq1, tq2);
        chartContainer.getChildren().clear();
        chartContainer.getChildren().add(ganttChart);
    }

}
