package ru.timeconqueror.fxmlobfuscator;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.ScrollToEvent;
import javafx.scene.input.*;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Dictionary {
    public static final Map<String, Class<?>> EVENT_ARGS_DICTIONARY = Stream.of(new Object[][]{
            //Main
            {"Action", ActionEvent.class},
            //Drag Drop
            {"DragDetected", MouseEvent.class},
            {"DragDone", DragEvent.class},
            {"DragDropped", DragEvent.class},
            {"DragEntered", DragEvent.class},
            {"DragExited", DragEvent.class},
            {"DragOver", DragEvent.class},
            {"MouseDragEntered", MouseDragEvent.class},
            {"MouseDragExited", MouseDragEvent.class},
            {"MouseDragOver", MouseDragEvent.class},
            {"MouseDragReleased", MouseDragEvent.class},
            //KeyBoard
            {"InputMethodTextChanged", InputMethodEvent.class},
            {"KeyPressed", InputMethodEvent.class},
            {"KeyReleased", KeyEvent.class},
            {"KeyTyped", KeyEvent.class},
            //Mouse
            {"ContextMenuRequested", ContextMenuEvent.class},
            {"MouseClicked", MouseEvent.class},
            {"MouseDragged", MouseEvent.class},
            {"MouseEntered", MouseEvent.class},
            {"MouseExited", MouseEvent.class},
            {"MouseMoved", MouseEvent.class},
            {"MousePressed", MouseEvent.class},
            {"MouseReleased", MouseEvent.class},
            {"Scroll", ScrollEvent.class},
            {"ScrollStarted", ScrollEvent.class},
            {"ScrollFinished", ScrollEvent.class},
            {"ScrollTo", ScrollToEvent.class},
            {"ScrollToColumn", ScrollToEvent.class},
            //Rotation
            {"Rotate", RotateEvent.class},
            {"RotationStarted", RotateEvent.class},
            {"RotationFinished", RotateEvent.class},
            //Swipe
            {"SwipeDown", SwipeEvent.class},
            {"SwipeLeft", SwipeEvent.class},
            {"SwipeRight", SwipeEvent.class},
            {"SwipeUp", SwipeEvent.class},
            //Touch
            {"TouchMoved", TouchEvent.class},
            {"TouchPressed", TouchEvent.class},
            {"TouchReleased", TouchEvent.class},
            {"TouchStationary", TouchEvent.class},
            //Zoom
            {"Zoom", ZoomEvent.class},
            {"ZoomStarted", ZoomEvent.class},
            {"ZoomFinished", ZoomEvent.class},
            //Combo Box Base
            {"Hidden", Event.class},
            {"Hiding", Event.class},
            {"Showing", Event.class},
            {"Shown", Event.class},
    }).collect(Collectors.collectingAndThen(
            Collectors.toMap(data -> (String) data[0], data -> (Class<?>) data[1]),
            Collections::unmodifiableMap));
}
