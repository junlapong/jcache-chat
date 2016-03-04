var stompClient = null;
var username = null;

function setConnected(connected) {
    if (connected) {
        $('#join').hide();
        $('#leave').show();
        $('#room-container').show();
        $('#message-container').show();
        $('#user-container').show();
    }
    else {
        $('#join').show();
        $('#leave').hide();
        $('#room-container').hide();
        $('#message-container').hide();
        $('#user-container').hide();
    }
}

function connect() {
    username = $('#username').val();
    $('#welcome').text('Welcome ' + username);
    setConnected(true);
    var socket = new SockJS('/chatEndpoint');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/rooms/created', function (room) {
            roomCreated(JSON.parse(room.body));
        });
        stompClient.subscribe('/topic/rooms/removed', function (room) {
            roomRemoved(JSON.parse(room.body));
        });
        stompClient.subscribe('/topic/users/joined', function (user) {
            userJoined(JSON.parse(user.body));
        });
        stompClient.subscribe('/topic/users/left', function (user) {
            userLeft(JSON.parse(user.body));
        });
        stompClient.subscribe('/topic/messages', function (message) {
            roomMessage(JSON.parse(message.body));
        });
    });

    $.ajax({
        url: "/users/" + encodeURIComponent(username),
        dataType: "json",
        type: "POST",
        contentType: "application/json",
        success:function(rooms) {
            $.each(rooms, function(index, room) {
                roomCreated(room);
            });

            $.ajax({
                url: "/users",
                dataType: "json",
                type: "GET",
                contentType: "application/json",
                success:function(users) {
                    $.each(users, function(index, user) {
                        userJoined(user);
                    });
                }
            });
        }
    });

    console.log("Connected");
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    $('#welcome').text('');
    setConnected(false);
    $("#room-list").empty();
    $("#room-messages").empty();
    $("#user-container-inner").empty();

    if(isNotEmpty(username)){
        $.ajax({
            url: "/users/"  + encodeURIComponent(username),
            dataType: "json",
            type: "DELETE",
            contentType: "application/json",
            data: {},
            success:function() {
                username = null;
                console.log('Disconnected: ' + frame);
            }
        });
    };

    console.log("Disconnected");
}

function roomCreated(room) {
    $("#rooms ul").append('<li id="roomid-li-' + room.id + '" role="presentation"><a id="roomid-a-' + room.id + '" href="#">' + room.name + ' <span id="roomid-li-' + room.id + '-span" class="badge">0</span></a></li>');
    $("#roomid-a-" + room.id).click(function(){
        showRoom(room.id);
    });
    roomMessagesCreated(room.id, room.messages);
}

function roomRemoved(room) {
    $("#rooms ul li#roomid-li-" + room.id).remove();
    $('#room-' + room.id + '-messages').remove();
}

function userJoined(user) {
    if($('#user-name-' + user).length) {
        console.log("User already added " + user);
    } else {
        var userTemplate = '<div id="user-name-' + user + '" style="">' + user +'</div>';
        $("#user-container-inner").append(userTemplate);
    }
}

function userLeft(user) {
    $("#user-container-inner #user-name-" + user).remove();
}

function roomMessagesCreated(roomId, messages) {
    var roomTemplate = '<div id="room-' + roomId + '-messages" style="display: none;">' +
                '  <form class="form-inline">' +
                '    <div class="form-group col-md-8" style="padding-bottom: 10px">' +
                '      <input type="text" class="form-control" style="width: 100%; min-width: 650px;max-width: 650px;" id="room-' + roomId + '-messages-message" placeholder="Enter your message here" autocomplete="off">' +
                '    </div>' +
                '  </form>' +
                '  <form class="form-inline">' +
                '    <div class="form-group col-md-8" style="padding-bottom: 10px">' +
                '      <textarea class="form-control" rows="6" style="width: 100%; min-width: 650px; max-width: 650px;" disabled></textarea>' +
                '    </div>' +
                '  </form>' +
                '  <form class="form-inline">' +
                '    <div class="form-group col-md-8">' +
                '      <a id="roomid-d-' + roomId + '" class="btn btn-danger" href="#" role="button"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span> Remove</a>' +
                '    </div>' +
                '  </form>' +
                '</div>';
    $("#room-messages").append(roomTemplate);
    $('#room-' + roomId + '-messages').hide();
    $('#room-' + roomId + '-messages form:nth-child(1)').submit(function (event) {
        var messageInput = $('#room-' + roomId + '-messages-message');
        createMessage(roomId, messageInput.val());
        messageInput.val('');
        event.preventDefault();
    });

    $("#roomid-d-" + roomId).click(function(){
        removeRoom(roomId);
    });

    $.each(messages, function(index, message) {
        roomMessage(message);
    });
}

function showRoom(roomId) {
    $('#roomid-a-' + roomId).tab('show');
    showRoomMessages(roomId);
}

function showRoomMessages(roomId) {
    $('#room-messages').children().hide()
    $('#room-' + roomId + '-messages').show();
}

function roomMessage(message) {
    var textAreaContent = $('#room-' + message.roomId + '-messages textarea').val();
    $('#room-' + message.roomId + '-messages textarea').val(textAreaContent + '[' + message.user + '] ' + message.text + '\r\n');
    var msgCounter = $('#roomid-li-' + message.roomId + '-span');
    msgCounter.text(parseInt(msgCounter.text()) + 1);
}

function createMessage(roomId, message) {
    $.ajax({
        url: "/room/" + encodeURIComponent(roomId) + "/message",
        dataType: "json",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({'text': message, 'roomId': roomId, 'user': username}),
        success:function() {
            console.log('Created message');
        }
    });
}

function createRoom(roomName) {
    $.ajax({
        url: "/room",
        dataType: "json",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({'name': roomName, 'user': username}),
        success:function() {
            console.log('Created room');
        }
    });
}

function removeRoom(roomId) {
    $.ajax({
        url: "/room/" + encodeURIComponent(roomId),
        dataType: "json",
        type: "DELETE",
        contentType: "application/json",
        data: roomId,
        success:function() {
            console.log('Removed room');
        }
    });
}

function isEmpty(str) {
    return (!str || 0 === str.length);
}

function isNotEmpty(str) {
    return !isEmpty(str);
}

$("#form-join").submit(function (event) {
    if(isNotEmpty($('#username').val())) {
        connect();
    }
    event.preventDefault();
});

$("#form-leave").submit(function (event) {
    disconnect();
    event.preventDefault();
});

$("#form-new-room").submit(function (event) {
    if(isNotEmpty($('#newRoom').val())) {
        createRoom($('#newRoom').val());
        $('#newRoom').val('');
    }
    event.preventDefault();
});

