import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class Deadline extends Task {
  private LocalDateTime by;

  public Deadline(String description, boolean mark, String by) throws DukeException {
    super(description, mark, 'D');

    if (by.isEmpty()) {
      throw new DukeException("The by of a deadline cannot be empty.");
    }
		try {
			this.by = DatetimeHelper.parse(by);
		} catch (DateTimeParseException e) {
			throw new InvalidDatetimeFormatException("by", "deadline");
		}
  }

  @Override
  public String toString() {
    return String.format("%s (by: %s)", super.toString(), DatetimeHelper.format(by));
  }

	@Override
	public String toCommand() {
		return String.format("%s /by %s", super.toCommand(), DatetimeHelper.commandFormat(by));
	}
}
