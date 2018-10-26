package com.example.athena.commands;

import com.example.athena.model.Entity;

public interface Command {

    void execute(Entity entity);
}
