package client.gui;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.*;
import server.database.*;
import java.util.*;


public class MainDashboardGUI extends Application {
    //the user who logged in - set before start() is called
    private User loggedInUser;
    
    public void setLoggedInUser(User user){
        this.loggedInUser = user;
    }
    
    @Override
    public void start(Stage dashboardStage){
        //top bar - common to all roles
            Label welcomeLabel = new Label();
            welcomeLabel.setText("Welcome, " + loggedInUser.getStudent_name() + " | Role: " + loggedInUser.getRole().name());
            
            Button logoutBtn = new Button();
            logoutBtn.setText("Logout");
            logoutBtn.setOnAction(e -> {
                //go back to login
                Stage loginStage = new Stage();
                LoginGUI lgin = new LoginGUI();
                lgin.start(loginStage);
                dashboardStage.close();
            });
            
            HBox topBar = new HBox();
            topBar.getChildren().addAll(welcomeLabel, logoutBtn);
            topBar.setSpacing(20);
            topBar.getStyleClass().add("top-bar");
            topBar.setPadding(new Insets(10));
        
        //content area - changes based on role
            VBox content = new VBox();
            content.setSpacing(10);
            content.setPadding(new Insets(10));
            
            String role = loggedInUser.getRole().name().toLowerCase();
            
            if(role.equals("tutee")){
                buildTuteeView(content);
            } else if(role.equals("tutor")){
                buildTutorView(content);
            } else if(role.equals("admin")){
                buildAdminView(content);
            }
        
        //scope
            BorderPane root = new BorderPane();
            root.setTop(topBar);
            root.setCenter(content);
            
            Scene scene = new Scene(root, 900, 600);
            //css
            scene.getStylesheets().add(getClass().getResource("/resources/style.css").toExternalForm());
            
        //stage management
            dashboardStage.setTitle("Dashboard");
            dashboardStage.setScene(scene);
            dashboardStage.show();
            
        //css
        //scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
    }
  //builds the tutee view - available sessions + my requests
private void buildTuteeView(VBox content){
    DBOperationsRemote db;
    try{
        db = rmi.RMIClient.getStub();     
    }catch(Exception except){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("Could not reach server: " + except.getMessage());
        alert.showAndWait();
        return;
    }
    
    //for ref bu session table 
    TableView<Booking> myRequestsTable = new TableView<>();
    
    //available sessions table
        Label availableLabel = new Label("Available Sessions");
        availableLabel.getStyleClass().add("section-title");
        
        TableView<TutoringSession> availableTable = new TableView<>();
        
        TableColumn<TutoringSession, Integer> sessionIdCol = new TableColumn<>("Session ID");
        sessionIdCol.setCellValueFactory(new PropertyValueFactory<>("session_id"));
        
        TableColumn<TutoringSession, String> subjectCol = new TableColumn<>("Subject");
        subjectCol.setCellValueFactory(new PropertyValueFactory<>("subject"));
        
        TableColumn<TutoringSession, String> tutorCol = new TableColumn<>("Tutor ID");
        tutorCol.setCellValueFactory(new PropertyValueFactory<>("tutor_id"));
        
        TableColumn<TutoringSession, String> datetimeCol = new TableColumn<>("Date & Time");
        datetimeCol.setCellValueFactory(new PropertyValueFactory<>("datetime"));
        
        TableColumn<TutoringSession, Integer> maxStudentsCol = new TableColumn<>("Max Students");
        maxStudentsCol.setCellValueFactory(new PropertyValueFactory<>("max_students"));
        
        TableColumn<TutoringSession, Void> requestCol = new TableColumn<>("Action");
        requestCol.setCellFactory(col -> new TableCell<>(){
            Button requestBtn = new Button("Request");
            {
                requestBtn.setOnAction(e -> {
                    TutoringSession selected = getTableView().getItems().get(getIndex());
                    Stage crudStage = new Stage();
                    CRUDBookingGUI bookingForm = new CRUDBookingGUI();
                    bookingForm.setSessionToBook(selected);
                    bookingForm.setLoggedInUser(loggedInUser);
                    bookingForm.setMode("request");
                    //refresh my requests table after confirming
                    bookingForm.setRefreshCallback(() -> {
                        List<Object> updatedBookings = safeSelectAll(db,"bookings");
                        ObservableList<Booking> myBookings = FXCollections.observableArrayList();
                        for(Object obj : updatedBookings){
                            Booking b = (Booking) obj;
                            if(b.getTutee_id().equals(loggedInUser.getStudent_id())){
                                myBookings.add(b);
                            }
                        }
                        myRequestsTable.setItems(myBookings);
                    });
                    bookingForm.start(crudStage);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty){
                super.updateItem(item, empty);
                setGraphic(empty ? null : requestBtn);
            }
        });
        
        availableTable.getColumns().addAll(sessionIdCol, subjectCol, tutorCol, datetimeCol, maxStudentsCol, requestCol);
        
        //load available sessions
        Runnable loadAvailable = () -> {
            List<Object> allSessions = safeSelectAll(db,"sessions");
            ObservableList<TutoringSession> availableSessions = FXCollections.observableArrayList();
            for(Object obj : allSessions){
                TutoringSession s = (TutoringSession) obj;
                if(s.getStatus() == SessionStatus.AVAILABLE){
                    availableSessions.add(s);
                }
            }
            availableTable.setItems(availableSessions);
        };
        loadAvailable.run();
        
        Button refreshAvailableBtn = new Button("Refresh");
        refreshAvailableBtn.setOnAction(e -> loadAvailable.run());
    
    //my requests table - declared here so the callback above can reference it
        Label myRequestsLabel = new Label("My Requests");
        
        TableColumn<Booking, Integer> bookingIdCol = new TableColumn<>("Booking ID");
        bookingIdCol.setCellValueFactory(new PropertyValueFactory<>("booking_id"));
        
        TableColumn<Booking, Integer> bookingSessionCol = new TableColumn<>("Session ID");
        bookingSessionCol.setCellValueFactory(new PropertyValueFactory<>("session_id"));
        
        TableColumn<Booking, String> bookingStatusCol = new TableColumn<>("Status");
        bookingStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        TableColumn<Booking, String> requestedAtCol = new TableColumn<>("Requested At");
        requestedAtCol.setCellValueFactory(new PropertyValueFactory<>("requested_at"));
        
        TableColumn<Booking, Void> cancelCol = new TableColumn<>("Action");
        cancelCol.setCellFactory(col -> new TableCell<>(){
            Button cancelBtn = new Button("Cancel");
            {
                cancelBtn.setOnAction(e -> {
                    Booking selected = getTableView().getItems().get(getIndex());
                    //update status to cancelled in db
                    selected.setStatus(BookingStatus.CANCELLED);
                    safeUpdate(db, selected);
                    //refresh the table from db
                    List<Object> updated = safeSelectAll(db,"bookings");
                    ObservableList<Booking> myBookings = FXCollections.observableArrayList();
                    for(Object obj : updated){
                        Booking b = (Booking) obj;
                        if(b.getTutee_id().equals(loggedInUser.getStudent_id())){
                            myBookings.add(b);
                        }
                    }
                    myRequestsTable.setItems(myBookings);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty){
                super.updateItem(item, empty);
                if(empty){
                    setGraphic(null);
                } else {
                    Booking b = getTableView().getItems().get(getIndex());
                    setGraphic(b.getStatus() == BookingStatus.PENDING ? cancelBtn : null);
                }
            }
        });
        
        myRequestsTable.getColumns().addAll(bookingIdCol, bookingSessionCol, bookingStatusCol, requestedAtCol, cancelCol);
        
        Runnable loadMyRequests = () -> {
            List<Object> allBookings = safeSelectAll(db,"bookings");
            ObservableList<Booking> myBookings = FXCollections.observableArrayList();
            for(Object obj : allBookings){
                Booking b = (Booking) obj;
                if(b.getTutee_id().equals(loggedInUser.getStudent_id())){
                    myBookings.add(b);
                }
            }
            myRequestsTable.setItems(myBookings);
        };
        loadMyRequests.run();
        
        Button refreshRequestsBtn = new Button("Refresh");
        refreshRequestsBtn.setOnAction(e -> loadMyRequests.run());
    
    content.getChildren().addAll(availableLabel, refreshAvailableBtn, availableTable, myRequestsLabel, refreshRequestsBtn, myRequestsTable);
}

//builds the tutor view - my sessions + booking requests
private void buildTutorView(VBox content){
    DBOperationsRemote db;
    try{
        db = rmi.RMIClient.getStub();     
    }catch(Exception except){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("Could not reach server: " + except.getMessage());
        alert.showAndWait();
        return;
    }
    
    //my sessions table
        Label mySessionsLabel = new Label("My Sessions");
        
        TableView<TutoringSession> mySessionsTable = new TableView<>();
        
        TableColumn<TutoringSession, Integer> sessionIdCol = new TableColumn<>("Session ID");
        sessionIdCol.setCellValueFactory(new PropertyValueFactory<>("session_id"));
        
        TableColumn<TutoringSession, String> subjectCol = new TableColumn<>("Subject");
        subjectCol.setCellValueFactory(new PropertyValueFactory<>("subject"));
        
        TableColumn<TutoringSession, String> datetimeCol = new TableColumn<>("Date & Time");
        datetimeCol.setCellValueFactory(new PropertyValueFactory<>("datetime"));
        
        TableColumn<TutoringSession, Integer> maxStudentsCol = new TableColumn<>("Max Students");
        maxStudentsCol.setCellValueFactory(new PropertyValueFactory<>("max_students"));
        
        TableColumn<TutoringSession, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        TableColumn<TutoringSession, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setCellFactory(col -> new TableCell<>(){
            Button editBtn = new Button("Edit");
            Button deleteBtn = new Button("Delete");
            HBox buttons = new HBox(5, editBtn, deleteBtn);
            {
                editBtn.setOnAction(e -> {
                    TutoringSession selected = getTableView().getItems().get(getIndex());
                    Stage crudStage = new Stage();
                    CRUDSessionGUI sessionForm = new CRUDSessionGUI();
                    sessionForm.setLoggedInUser(loggedInUser);
                    sessionForm.setSessionToEdit(selected);
                    sessionForm.setMode("edit");
                    //refresh after editing
                    sessionForm.setRefreshCallback(() -> {
                        List<Object> allSessions = safeSelectAll(db,"sessions");
                        ObservableList<TutoringSession> mySessions = FXCollections.observableArrayList();
                        for(Object obj : allSessions){
                            TutoringSession s = (TutoringSession) obj;
                            if(s.getTutor_id().equals(loggedInUser.getStudent_id())){
                                mySessions.add(s);
                            }
                        }
                        mySessionsTable.setItems(mySessions);
                    });
                    sessionForm.start(crudStage);
                });
                deleteBtn.setOnAction(e -> {
                    TutoringSession selected = getTableView().getItems().get(getIndex());
                    safeDelete(db, selected.getSession_id(), "sessions");
                    mySessionsTable.getItems().remove(selected);
                });
                deleteBtn.getStyleClass().add("button-danger");
            }
            @Override
            protected void updateItem(Void item, boolean empty){
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });
        
        mySessionsTable.getColumns().addAll(sessionIdCol, subjectCol, datetimeCol, maxStudentsCol, statusCol, actionsCol);
        
        Runnable loadMySessions = () -> {
            List<Object> allSessions = safeSelectAll(db,"sessions");
            ObservableList<TutoringSession> mySessions = FXCollections.observableArrayList();
            for(Object obj : allSessions){
                TutoringSession s = (TutoringSession) obj;
                if(s.getTutor_id().equals(loggedInUser.getStudent_id())){
                    mySessions.add(s);
                }
            }
            mySessionsTable.setItems(mySessions);
        };
        loadMySessions.run();
        
        Button createSessionBtn = new Button("Create New Session");
        createSessionBtn.setOnAction(e -> {
            Stage crudStage = new Stage();
            CRUDSessionGUI sessionForm = new CRUDSessionGUI();
            sessionForm.setLoggedInUser(loggedInUser);
            sessionForm.setMode("create");
            //refresh after creating
            sessionForm.setRefreshCallback(loadMySessions);
            sessionForm.start(crudStage);
        });
        
        Button refreshSessionsBtn = new Button("Refresh");
        refreshSessionsBtn.setOnAction(e -> loadMySessions.run());
    
    //booking requests table
        Label bookingRequestsLabel = new Label("Booking Requests");
        
        TableView<Booking> bookingRequestsTable = new TableView<>();
        
        TableColumn<Booking, Integer> bookingIdCol = new TableColumn<>("Booking ID");
        bookingIdCol.setCellValueFactory(new PropertyValueFactory<>("booking_id"));
        
        TableColumn<Booking, Integer> sessionIdCol2 = new TableColumn<>("Session ID");
        sessionIdCol2.setCellValueFactory(new PropertyValueFactory<>("session_id"));
        
        TableColumn<Booking, String> tuteeIdCol = new TableColumn<>("Tutee ID");
        tuteeIdCol.setCellValueFactory(new PropertyValueFactory<>("tutee_id"));
        
        TableColumn<Booking, String> bookingStatusCol = new TableColumn<>("Status");
        bookingStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        TableColumn<Booking, Void> approveCol = new TableColumn<>("Actions");
        approveCol.setCellFactory(col -> new TableCell<>(){
            Button approveBtn = new Button("Approve");
            Button declineBtn = new Button("Decline");
            HBox buttons = new HBox(5, approveBtn, declineBtn);
            {
                approveBtn.setOnAction(e -> {
                    Booking selected = getTableView().getItems().get(getIndex());
                    Stage crudStage = new Stage();
                    CRUDBookingGUI bookingForm = new CRUDBookingGUI();
                    bookingForm.setBookingToManage(selected);
                    bookingForm.setLoggedInUser(loggedInUser);
                    bookingForm.setMode("manage");
                    bookingForm.setRefreshCallback(() -> {
                        loadPendingBookings(bookingRequestsTable, db, loadMySessions);
                    });
                    bookingForm.start(crudStage);
                });
                approveBtn.getStyleClass().add("button-success");
                declineBtn.setOnAction(e -> {
                    Booking selected = getTableView().getItems().get(getIndex());
                    selected.setStatus(BookingStatus.CANCELLED);
                    safeUpdate(db,selected);
                    bookingRequestsTable.getItems().remove(selected);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty){
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });
        
        bookingRequestsTable.getColumns().addAll(bookingIdCol, sessionIdCol2, tuteeIdCol, bookingStatusCol, approveCol);
        
        Runnable loadBookingRequests = () -> loadPendingBookings(bookingRequestsTable, db, loadMySessions);
        loadBookingRequests.run();
        
        Button refreshBookingsBtn = new Button("Refresh");
        refreshBookingsBtn.setOnAction(e -> loadBookingRequests.run());
    
    content.getChildren().addAll(mySessionsLabel, createSessionBtn, refreshSessionsBtn, mySessionsTable, bookingRequestsLabel, refreshBookingsBtn, bookingRequestsTable);
}

private List<Object> safeSelectAll(DBOperationsRemote db, String table){
    try{
        return db.selectAllOperation(table);
    }catch(Exception ex){
        showConnectionError(ex);
        return new ArrayList<>();
    }
}
private void safeUpdate(DBOperationsRemote db, Object obj){
    try{ 
        db.updateOperation(obj); 
    }catch(Exception ex){ 
        showConnectionError(ex); 
    }
}
private void safeDelete(DBOperationsRemote db, Object id, String table){
    try{ 
        db.deleteOperation(id, table); 
    }catch(Exception ex){ 
        showConnectionError(ex); 
    }
}
private void showConnectionError(Exception ex){
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setContentText("Could not reach server: " + ex.getMessage());
    alert.showAndWait();
}

//helper for loading pending bookings into the tutor's booking requests table
private void loadPendingBookings(TableView<Booking> table, DBOperationsRemote db, Runnable loadMySessions){
    List<Object> allSessions = safeSelectAll(db,"sessions");
    java.util.Set<Integer> mySessionIds = new java.util.HashSet<>();
    for(Object obj : allSessions){
        TutoringSession s = (TutoringSession) obj;
        if(s.getTutor_id().equals(loggedInUser.getStudent_id())){
            mySessionIds.add(s.getSession_id());
        }
    }
    List<Object> allBookings = safeSelectAll(db,"bookings");
    ObservableList<Booking> pendingBookings = FXCollections.observableArrayList();
    for(Object obj : allBookings){
        Booking b = (Booking) obj;
        if(mySessionIds.contains(b.getSession_id()) && b.getStatus() == BookingStatus.PENDING){
            pendingBookings.add(b);
        }
    }
    table.setItems(pendingBookings);
}

//builds the admin view - tabs for users, sessions, bookings + stats
private void buildAdminView(VBox content){
    DBOperationsRemote db;
    try{
        db = rmi.RMIClient.getStub();     
    }catch(Exception except){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("Could not reach server: " + except.getMessage());
        alert.showAndWait();
        return;
    }
    
    //stats label - updated on refresh
        Label statsLabel = new Label();
        
        Runnable updateStats = () -> {
            int users = safeSelectAll(db,"users").size();
            int sessions = safeSelectAll(db,"sessions").size();
            int bookings = safeSelectAll(db,"bookings").size();
            statsLabel.setText("Stats — Users: " + users + " | Sessions: " + sessions + " | Bookings: " + bookings);
            statsLabel.getStyleClass().add("stats-label");
        };
        updateStats.run();
    
    //tabs
        TabPane tabPane = new TabPane();
        
        //users tab
        Tab usersTab = new Tab("Users");
        usersTab.setClosable(false);
        VBox usersBox = new VBox(5);
        
        TableView<User> usersTable = new TableView<>();
        
        TableColumn<User, String> userIdCol = new TableColumn<>("Student ID");
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("student_id"));
        
        TableColumn<User, String> userNameCol = new TableColumn<>("Name");
        userNameCol.setCellValueFactory(new PropertyValueFactory<>("student_name"));
        
        TableColumn<User, String> userEmailCol = new TableColumn<>("Email");
        userEmailCol.setCellValueFactory(new PropertyValueFactory<>("student_email"));
        
        TableColumn<User, String> userRoleCol = new TableColumn<>("Role");
        userRoleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        
        TableColumn<User, Void> userActionsCol = new TableColumn<>("Actions");
        userActionsCol.setCellFactory(col -> new TableCell<>(){
            Button deleteBtn = new Button("Delete");
            {
                deleteBtn.setOnAction(e -> {
                    User selected = getTableView().getItems().get(getIndex());
                    safeDelete(db, selected.getStudent_id(), "users");
                    usersTable.getItems().remove(selected);
                    updateStats.run();
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty){
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteBtn);
            }
        });
        
        usersTable.getColumns().addAll(userIdCol, userNameCol, userEmailCol, userRoleCol, userActionsCol);
        
        Runnable loadUsers = () -> {
            ObservableList<User> userList = FXCollections.observableArrayList();
            for(Object obj : safeSelectAll(db,"users")){ userList.add((User) obj); }
            usersTable.setItems(userList);
        };
        loadUsers.run();
        
        Button createUserBtn = new Button("Create User");
        createUserBtn.setOnAction(e -> {
            Stage signupStage = new Stage();
            SignupGUI sgup = new SignupGUI();
            sgup.start(signupStage);
        });
        
        Button refreshUsersBtn = new Button("Refresh");
        refreshUsersBtn.setOnAction(e -> { loadUsers.run(); updateStats.run(); });
        
        HBox usersBtnRow = new HBox(5, createUserBtn, refreshUsersBtn);
        usersBox.getChildren().addAll(usersBtnRow, usersTable);
        usersTab.setContent(usersBox);
        
        //sessions tab
        Tab sessionsTab = new Tab("Sessions");
        sessionsTab.setClosable(false);
        VBox sessionsBox = new VBox(5);
        
        TableView<TutoringSession> sessionsTable = new TableView<>();
        
        TableColumn<TutoringSession, Integer> sIdCol = new TableColumn<>("ID");
        sIdCol.setCellValueFactory(new PropertyValueFactory<>("session_id"));
        
        TableColumn<TutoringSession, String> sSubjectCol = new TableColumn<>("Subject");
        sSubjectCol.setCellValueFactory(new PropertyValueFactory<>("subject"));
        
        TableColumn<TutoringSession, String> sTutorCol = new TableColumn<>("Tutor ID");
        sTutorCol.setCellValueFactory(new PropertyValueFactory<>("tutor_id"));
        
        TableColumn<TutoringSession, String> sStatusCol = new TableColumn<>("Status");
        sStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        TableColumn<TutoringSession, Void> sActionsCol = new TableColumn<>("Actions");
        
        Runnable loadSessions = () -> {
            ObservableList<TutoringSession> sessionList = FXCollections.observableArrayList();
            for(Object obj : safeSelectAll(db,"sessions")){ sessionList.add((TutoringSession) obj); }
            sessionsTable.setItems(sessionList);
        };
        
        sActionsCol.setCellFactory(col -> new TableCell<>(){
            Button editBtn = new Button("Edit");
            Button deleteBtn = new Button("Delete");
            HBox buttons = new HBox(5, editBtn, deleteBtn);
            {
                editBtn.setOnAction(e -> {
                    TutoringSession selected = getTableView().getItems().get(getIndex());
                    Stage crudStage = new Stage();
                    CRUDSessionGUI sessionForm = new CRUDSessionGUI();
                    sessionForm.setLoggedInUser(loggedInUser);
                    sessionForm.setSessionToEdit(selected);
                    sessionForm.setMode("edit");
                    sessionForm.setRefreshCallback(() -> { loadSessions.run(); updateStats.run(); });
                    sessionForm.start(crudStage);
                });
                deleteBtn.setOnAction(e -> {
                    TutoringSession selected = getTableView().getItems().get(getIndex());
                    safeDelete(db, selected.getSession_id(), "sessions");
                    sessionsTable.getItems().remove(selected);
                    updateStats.run();
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty){
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });
        
        sessionsTable.getColumns().addAll(sIdCol, sSubjectCol, sTutorCol, sStatusCol, sActionsCol);
        loadSessions.run();
        
        Button createSessionBtn = new Button("Create Session");
        createSessionBtn.setOnAction(e -> {
            Stage crudStage = new Stage();
            CRUDSessionGUI sessionForm = new CRUDSessionGUI();
            sessionForm.setLoggedInUser(loggedInUser);
            sessionForm.setMode("create");
            sessionForm.setRefreshCallback(() -> { loadSessions.run(); updateStats.run(); });
            sessionForm.start(crudStage);
        });
        
        Button refreshSessionsBtn = new Button("Refresh");
        refreshSessionsBtn.setOnAction(e -> { loadSessions.run(); updateStats.run(); });
        
        HBox sessionsBtnRow = new HBox(5, createSessionBtn, refreshSessionsBtn);
        sessionsBox.getChildren().addAll(sessionsBtnRow, sessionsTable);
        sessionsTab.setContent(sessionsBox);
        
        //bookings tab
        Tab bookingsTab = new Tab("Bookings");
        bookingsTab.setClosable(false);
        VBox bookingsBox = new VBox(5);
        
        TableView<Booking> bookingsTable = new TableView<>();
        
        TableColumn<Booking, Integer> bIdCol = new TableColumn<>("Booking ID");
        bIdCol.setCellValueFactory(new PropertyValueFactory<>("booking_id"));
        
        TableColumn<Booking, Integer> bSessionCol = new TableColumn<>("Session ID");
        bSessionCol.setCellValueFactory(new PropertyValueFactory<>("session_id"));
        
        TableColumn<Booking, String> bTuteeCol = new TableColumn<>("Tutee ID");
        bTuteeCol.setCellValueFactory(new PropertyValueFactory<>("tutee_id"));
        
        TableColumn<Booking, String> bStatusCol = new TableColumn<>("Status");
        bStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        TableColumn<Booking, Void> bActionsCol = new TableColumn<>("Actions");
        
        Runnable loadBookings = () -> {
            ObservableList<Booking> bookingList = FXCollections.observableArrayList();
            for(Object obj : safeSelectAll(db,"bookings")){ bookingList.add((Booking) obj); }
            bookingsTable.setItems(bookingList);
        };
        
        bActionsCol.setCellFactory(col -> new TableCell<>(){
            Button deleteBtn = new Button("Delete");
            {
                deleteBtn.setOnAction(e -> {
                    Booking selected = getTableView().getItems().get(getIndex());
                    safeDelete(db, selected.getBooking_id(), "bookings");
                    bookingsTable.getItems().remove(selected);
                    updateStats.run();
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty){
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteBtn);
            }
        });
        
        bookingsTable.getColumns().addAll(bIdCol, bSessionCol, bTuteeCol, bStatusCol, bActionsCol);
        loadBookings.run();
        
        Button refreshBookingsBtn = new Button("Refresh");
        refreshBookingsBtn.setOnAction(e -> { loadBookings.run(); updateStats.run(); });
        
        bookingsBox.getChildren().addAll(refreshBookingsBtn, bookingsTable);
        bookingsTab.setContent(bookingsBox);
        
        tabPane.getTabs().addAll(usersTab, sessionsTab, bookingsTab);
    
    content.getChildren().addAll(statsLabel, tabPane);
}
    public static void main(String[] args){
        launch(args);
    }
}