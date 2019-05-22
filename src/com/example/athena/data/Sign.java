package com.example.athena.data;

import com.badlogic.gdx.math.Rectangle;

public class Sign {

    private Rectangle rectangle;
    private String text;

    public Sign(final Rectangle rect, final String text) {
        this.rectangle = rect;
        this.text = text;
    }

    //TODO im Schulrepo ist das NUR String.
    //Formatter Zeichen (#) fehlt bei denen auch NOCH in der Map
    public String[] getText() {
        return new String[] {this.text};
    }
}
