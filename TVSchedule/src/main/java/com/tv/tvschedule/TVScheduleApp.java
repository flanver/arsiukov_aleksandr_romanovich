package com.tv.tvschedule;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.ObservableList;

import java.util.*;

public class TVScheduleApp extends Application {
    private TableView<Program> programTable;
    private Map<String, List<Program>> schedule;
    private ObservableList<String> genres;

    public TVScheduleApp() {
        this.schedule = new HashMap<>();
        this.genres = FXCollections.observableArrayList("Фильм", "Сериал", "Документальный", "Новости", "Спорт", "Музыка");
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane borderPane = new BorderPane();

        programTable = new TableView<>();
        programTable.setEditable(true);

        TableColumn<Program, String> channelCol = new TableColumn<>("Канал");
        channelCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getChannel()));

        TableColumn<Program, String> dayOfWeekCol = new TableColumn<>("День недели");
        dayOfWeekCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDayOfWeek()));

        TableColumn<Program, String> startTimeCol = new TableColumn<>("Время начала");
        startTimeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStartTime()));

        TableColumn<Program, String> genreCol = new TableColumn<>("Жанр");
        genreCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGenre()));

        programTable.getColumns().addAll(channelCol, dayOfWeekCol, startTimeCol, genreCol);

        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);
        inputGrid.setPadding(new Insets(10));

        TextField channelInput = new TextField();
        TextField dayOfWeekInput = new TextField();
        TextField startTimeInput = new TextField();
        ComboBox<String> genreComboBox = new ComboBox<>(genres);

        Button addButton = new Button("Добавить программу");
        Button editButton = new Button("Изменить программу");
        Button deleteButton = new Button("Удалить программу");

        inputGrid.add(new Label("Канал"), 0, 0);
        inputGrid.add(channelInput, 1, 0);
        inputGrid.add(new Label("День недели"), 0, 1);
        inputGrid.add(dayOfWeekInput, 1, 1);
        inputGrid.add(new Label("Время начала"), 0, 2);
        inputGrid.add(startTimeInput, 1, 2);
        inputGrid.add(new Label("Жанр"), 0, 3);
        inputGrid.add(genreComboBox, 1, 3);
        inputGrid.add(addButton, 1, 4);
        inputGrid.add(editButton, 1, 5);
        inputGrid.add(deleteButton, 1, 6);

        addButton.setOnAction(event -> {
            String channel = channelInput.getText();
            String dayOfWeek = dayOfWeekInput.getText();
            String startTime = startTimeInput.getText();
            String genre = genreComboBox.getValue();
            if (validateAndAddProgram(channel, dayOfWeek, startTime, genre)) {
                channelInput.clear();
                dayOfWeekInput.clear();
                startTimeInput.clear();
            }
        });

        editButton.setOnAction(event -> {
            Program selectedProgram = programTable.getSelectionModel().getSelectedItem();
            if (selectedProgram != null) {
                showEditProgramDialog(selectedProgram);
            } else {
                showAlert("No Program Selected", "Please select a program to edit.");
            }
        });

        deleteButton.setOnAction(event -> {
            Program selectedProgram = programTable.getSelectionModel().getSelectedItem();
            deleteProgram(selectedProgram);
        });

        borderPane.setCenter(programTable);
        borderPane.setRight(inputGrid);

        Scene scene = new Scene(borderPane, 600, 400);
        primaryStage.setTitle("Программа телеканалов");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private boolean addProgram(String channel, String dayOfWeek, String startTime, String genre) {
        if (channel.isEmpty() || dayOfWeek.isEmpty() || startTime.isEmpty() || Objects.isNull(genre)) {
            showAlert("Ошибка", "Заполните все поля!");
            return false;
        }
        Program program = new Program(channel, dayOfWeek, startTime, genre);
        if (schedule.containsKey(dayOfWeek)) {
            schedule.get(dayOfWeek).add(program);
        } else {
            List<Program> programs = new ArrayList<>();
            programs.add(program);
            schedule.put(dayOfWeek, programs);
        }
        updateProgramTable();
        return true;
    }

    private void showEditProgramDialog(Program program) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20));

        TextField channelInput = new TextField(program.getChannel());
        TextField dayOfWeekInput = new TextField(program.getDayOfWeek());
        TextField startTimeInput = new TextField(program.getStartTime());
        ComboBox<String> genreComboBox = new ComboBox<>(genres);
        genreComboBox.setValue(program.getGenre());
        Button saveButton = new Button("Сохранить");

        gridPane.add(new Label("Канал:"), 0, 0);
        gridPane.add(channelInput, 1, 0);
        gridPane.add(new Label("День недели:"), 0, 1);
        gridPane.add(dayOfWeekInput, 1, 1);
        gridPane.add(new Label("Время начала:"), 0, 2);
        gridPane.add(startTimeInput, 1, 2);
        gridPane.add(new Label("Жанр:"), 0, 3);
        gridPane.add(genreComboBox, 1, 3);
        gridPane.add(saveButton, 1, 4);

        saveButton.setOnAction(event -> {
            String channel = channelInput.getText();
            String dayOfWeek = dayOfWeekInput.getText();
            String startTime = startTimeInput.getText();
            String genre = genreComboBox.getValue();
            Program updatedProgram = new Program(channel, dayOfWeek, startTime, genre);
            if (validateAndAddProgram(updatedProgram.getChannel(), updatedProgram.getDayOfWeek(),
                    updatedProgram.getStartTime(), updatedProgram.getGenre())) {
                deleteProgram(program);

                channelInput.clear();
                dayOfWeekInput.clear();
                startTimeInput.clear();
                dialogStage.close();
            }
        });

        Scene scene = new Scene(gridPane, 300, 200);
        dialogStage.setScene(scene);
        dialogStage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void deleteProgram(Program program) {
        for (List<Program> programs : schedule.values()) {
            programs.remove(program);
        }
        updateProgramTable();
    }

    private void updateProgramTable() {
        List<Program> allPrograms = new ArrayList<>();
        schedule.values().forEach(allPrograms::addAll);
        programTable.setItems(FXCollections.observableArrayList(allPrograms));
    }

    private boolean validateAndAddProgram(String channel, String dayOfWeek, String startTime, String genre) {
        if (!startTime.matches("\\d{1,2}:\\d{2}|\\d{1,2}")) {
            showAlert("Ошибка ввода", "Пожалуйста, введите время в формате ЧЧ:ММ или ЧЧ");
            return false;
        }
        return addProgram(channel, dayOfWeek, startTime, genre);
    }

    private boolean validateAndEditProgram(Program program, String channel, String dayOfWeek, String startTime, String genre) {
        if (!startTime.matches("\\d{1,2}:\\d{2}|\\d{1,2}")) {
            showAlert("Ошибка ввода", "Пожалуйста, введите время в формате ЧЧ:ММ или ЧЧ");
            return false;
        }
        deleteProgram(program);
        return addProgram(channel, dayOfWeek, startTime, genre);
    }

    public static void main(String[] args) {
        launch(args);
    }
}