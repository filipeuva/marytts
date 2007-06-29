package de.dfki.lt.mary.unitselection.voiceimport;

import java.io.*;
import java.util.*;

/**
 * Makes the file etc/.txt.done.data
 * from the text files in text directory
 * 
 * @author Anna Hunecke
 *
 */
public class Mary2FestvoxTranscripts extends VoiceImportComponent{
    
    private DatabaseLayout db;
    private BasenameList baseNameList;
    
    public final String TRANSCRIPTFILE = "mary2FestvoxTranscripts.transcriptFile";
    
    public String getName(){
        return "mary2FestvoxTranscripts";
    }
    
     public void initialise( BasenameList setbnl, SortedMap newProps )
    {
         this.baseNameList = setbnl;
        this.props = newProps;
    }
    
     public SortedMap getDefaultProps(DatabaseLayout db){
         this.db = db;
       if (props == null){
           props = new TreeMap();
           props.put(TRANSCRIPTFILE,db.getProp(db.ROOTDIR)
           				+"txt.done.data");
       }
       return props;
   }
    
    public boolean compute(){
        try{
        //open output file
        PrintWriter textOut =
            new PrintWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(
                                    new File(getProp(TRANSCRIPTFILE))),"UTF-8"));
        
        //go through the text files
        String[] basenames = baseNameList.getListAsArray();
        String textDir = db.getProp(db.TEXTDIR);
        String textExt = db.getProp(db.TEXTEXT);
        for (int i=0;i<basenames.length;i++){
            //open the next file
            try{
                File nextFile = new File(textDir+basenames[i]+textExt);
                BufferedReader fileIn =
                    new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(nextFile),"UTF-8"));
            
                String line = fileIn.readLine().trim();
                //line = line.replaceAll("\"","=");
                fileIn.close();
                textOut.println("( "+basenames[i]+" \""+line+"\" )");
            }catch(FileNotFoundException fnfe){
                baseNameList.remove(basenames[i]);
                continue;
            }
        }
        textOut.flush();
        textOut.close();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public int getProgress(){
        return -1;
    }
    
}
