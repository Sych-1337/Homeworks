package sych.homework.helloworld;

import com.google.gson.annotations.SerializedName;

public class BalanceResponce {

    private String status;

    @SerializedName("total_expenses")
    private float totalExpences;

    @SerializedName("total_income")
    private float totalIncome;

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public float getTotalExpences() {
        return totalExpences;
    }

    public void setTotalExpences(final float totalExpences) {
        this.totalExpences = totalExpences;
    }

    public float getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(final float totalIncome) {
        this.totalIncome = totalIncome;
    }
}
