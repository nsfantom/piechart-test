package tm.nsfantom.piechart.ui.schedule;

public class ScheduleModel {

    private String title="";
    private String[] periods = new String[0];
    private String[] days = new String[0];

    public ScheduleModel() {
    }

    public ScheduleModel(String[] periods, String[] days) {
        this.periods = periods;
        this.days = days;
    }

    public ScheduleModel setTitle(String title) {
        this.title = title;
        return this;
    }

    public ScheduleModel setPeriods(String... periods) {
        this.periods = periods;
        return this;
    }

    public ScheduleModel setDays(String... days) {
        this.days = days;
        return this;
    }

    public String[] getPeriods() {
        return periods;
    }

    public String[] getDays() {
        return days;
    }

    public String getTitle() {
        return title;
    }
}
