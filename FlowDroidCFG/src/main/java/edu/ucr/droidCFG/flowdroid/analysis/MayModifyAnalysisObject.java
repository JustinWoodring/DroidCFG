package edu.ucr.droidCFG.flowdroid.analysis;

public class MayModifyAnalysisObject {
    private String value;

    MayModifyAnalysisObject(){
        value="";
    }
    public MayModifyAnalysisObject(String text){
        value = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String input){
        value = input;
    }
}
