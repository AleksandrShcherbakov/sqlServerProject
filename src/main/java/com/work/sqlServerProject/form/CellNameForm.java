package com.work.sqlServerProject.form;

/**
 * Created by a.shcherbakov on 19.02.2019.
 */
public class CellNameForm {

    private String formCellName;
    private int formCI;

    public CellNameForm() {
    }

    public CellNameForm(int formCI) {

        this.formCI = formCI;
    }

    public String getFormCellName() {
        return formCellName;
    }

    public void setFormCellName(String formCellName) {
        this.formCellName = formCellName;
    }

    public int getFormCI() {
        return formCI;
    }

    public void setFormCI(int formCI) {
        this.formCI = formCI;
    }
}
