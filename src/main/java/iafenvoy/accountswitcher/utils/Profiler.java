package iafenvoy.accountswitcher.utils;

import org.jetbrains.annotations.NotNull;

public class Profiler {
    String location = null;

    public void push(@NotNull String location) {
        this.location = location;
    }

    public void swap(@NotNull String location) {
        this.location = location;
    }

    public void pop() {
        if (this.location == null)
            throw new IllegalStateException("You should push before pop");
        this.location = null;
    }

    public String getLocation() {
        return location;
    }
}
