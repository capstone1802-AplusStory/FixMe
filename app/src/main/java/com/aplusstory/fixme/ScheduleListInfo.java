package com.aplusstory.fixme;

public class ScheduleListInfo {

    public static int getColorIconCode(int schDataColorCode){
        switch(schDataColorCode){
            case ScheduleDataManager.TableColor.YELLOWGREEN:
                return R.drawable.ic_yellowgreen_circle;
            case ScheduleDataManager.TableColor.SKYBLUE:
                return R.drawable.ic_skyblue_circle;
            case ScheduleDataManager.TableColor.PINK:
                return R.drawable.ic_pink_circle;
            case ScheduleDataManager.TableColor.YELLOW:
                return R.drawable.ic_yellow_circle;
            case ScheduleDataManager.TableColor.PURPLE:
                return R.drawable.ic_purple_circle;
            case ScheduleDataManager.TableColor.BLUE:
                return R.drawable.ic_blue_circle;
            case ScheduleDataManager.TableColor.MINT:
                return R.drawable.ic_mint_circle;
            case ScheduleDataManager.TableColor.GREEN:
                return R.drawable.ic_green_circle;
            case ScheduleDataManager.TableColor.RED:
                return R.drawable.ic_red_circle_padding;
            default:
                return -1;
        }
    }

    public int scheduleColor;
    public String scheduleName;

    public ScheduleListInfo(int scheduleColor, String scheduleName) {
        this.scheduleColor = scheduleColor;
        this.scheduleName = scheduleName;
    }
}
