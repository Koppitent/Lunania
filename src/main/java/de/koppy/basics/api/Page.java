package de.koppy.basics.api;

public class Page {

    private String[] page = {"", "", "", "", "", "", "", "", "", "", "", "", "", ""};
    private int maxrows = 14;

    public Page() { }

    public String getPage() {
        String out = "";
        for(String row : page) {
            out = out + row + "\n";
        }
        return out;
    }

    public void addToPage(String data) {
        int index = 0;
        for(String row : page) {
            if(row.isEmpty()) {
                page[index] = data;
                break;
            }
            index++;
        }
    }

    public void removeFromPage(int row) {
        if(page[row] != null) {
            page[row] = "";
        }
    }

    public void setRow(int row, String data) {
        if(row <= maxrows) {
            page[row] = data;
        }
    }

}