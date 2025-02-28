package duke.task;

import java.time.LocalDateTime;
import java.util.Map;

import duke.exception.DukeException;
import duke.exception.InvalidDatetimeFormatException;
import duke.helper.DatetimeHelper;

/**
 * Represents a Event task, with an additional from and to datetime in addition to Task arguments.
 */
public class Event extends Task {
    private LocalDateTime from;
    private LocalDateTime to;

    /**
     * Returns an Event
     *
     * @param description the task description
     * @param isMarked if the task is marked
     * @param from the from deadline
     * @param to the to deadline
     * @return the constructed Event
     * @throws DukeException if from or to is empty, or from is after to
     * @throws InvalidDatetimeFormatException if from or to is not valid datetime
     */
    public Event(String description, boolean isMarked, String from, String to) {
        super(description, isMarked, 'E');

        this.from = DatetimeHelper.parseField(from, "from", "event");
        this.to = DatetimeHelper.parseField(to, "to", "event");
        validateFields(this.from, this.to);
    }

    private void validateFields(LocalDateTime from, LocalDateTime to) {
        if (from.isAfter(to)) {
            throw new DukeException("The from of an event must be before its to");
        }
    }

    /**
     * Returns a string representation of Event. Adds from and to argument to the task string.
     *
     * @return a string representation of Event
     */
    @Override
    public String toString() {
        return String.format(
            "%s (from: %s to: %s)",
            super.toString(), DatetimeHelper.format(from), DatetimeHelper.format(to));
    }

    /**
     * Returns a string representation of the command used to construct Event
     *
     * @return a string of the command used.
     */
    @Override
    public String toCommand() {
        return String.format(
            "%s /from %s /to %s",
            super.toCommand(), DatetimeHelper.commandFormat(from), DatetimeHelper.commandFormat(to));
    }

    @Override
    protected boolean before(LocalDateTime before) {
        return from.isBefore(before);
    }

    @Override
    public void edit(Map<String, String> arguments) {
        if ((!arguments.containsKey("from")) && (!arguments.containsKey("to"))) {
            throw new DukeException("event requires a from or/and to argument");
        }

        LocalDateTime from = this.from;
        LocalDateTime to = this.to;
        if (arguments.containsKey("from")) {
            from = DatetimeHelper.parseField(arguments.get("from"), "from", "event");
        }
        if (arguments.containsKey("to")) {
            to = DatetimeHelper.parseField(arguments.get("to"), "to", "event");
        }

        validateFields(from, to);
        this.from = from;
        this.to = to;
    }
}
