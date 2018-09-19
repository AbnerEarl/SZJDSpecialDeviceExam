package com.example.frank.jinding.UI.PublicMethodActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.frank.jinding.Bean.Report.SzjdBzQz01;
import com.example.frank.jinding.Bean.Report.SzjdBzQz02;
import com.example.frank.jinding.Bean.Report.SzjdBzQz04;
import com.example.frank.jinding.Bean.Report.SzjdBzQz05;
import com.example.frank.jinding.Bean.Report.SzjdBzQz15;
import com.example.frank.jinding.Bean.Report.SzjdBzQz19;
import com.example.frank.jinding.Bean.Report.SzjdBzQz22;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Report.ReportSzjdBzQz15;
import com.example.frank.jinding.Service.ApiService;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportActivity extends AppCompatActivity {
    private String nameStr = "";

    private Range range = null;
    private HWPFDocument hwpf = null;

    private String htmlPath;
    private String picturePath;

    private WebView view;

    private List pictures;

    private TableIterator tableIterator;

    private int presentPicture = 0;

    private int screenWidth;
    private String device_detail_id="";

    private FileOutputStream output;
    private  String filecode;
    private File myFile;
    private SzjdBzQz15 szjdBzQz15;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        view = (WebView)findViewById(R.id.webview_report);
        screenWidth = this.getWindowManager().getDefaultDisplay().getWidth() - 10;
        Intent intent = getIntent();
        filecode= intent.getStringExtra("filecode");
        device_detail_id=intent.getStringExtra("device_detail_id");

        Toast.makeText(ReportActivity.this,device_detail_id,Toast.LENGTH_SHORT).show();
        //nameStr= ReportFilePath.rawFilePath(ReportActivity.this,filecode);
        Toast.makeText(ReportActivity.this,filecode,Toast.LENGTH_SHORT).show();

        nameStr= Environment.getExternalStorageDirectory() + "/Luban/documents/SZJD-BZ-QZ-15.doc";
        getReportData();
    }



    private void getReportData(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {



                    Map<String, Object> paremetes = new HashMap<>();
                    paremetes.put("data",device_detail_id);
                    ApiService.GetString(ReportActivity.this, "getReportInfo", paremetes, new RxStringCallback() {

                        @Override
                        public void onNext(Object tag, String response) {

                            //refreshLayout.setRefreshing(false);
                            if (!response.trim().equals("获取失败！")&&!response.trim().equals("暂时没有报告")) {
                                //SzjdBzQz15 szjdBzQz15 = (SzjdBzQz15) JSONObject.toBean(object, SearchRule.class);
                                JSONObject reportObject= JSON.parseObject(response);
                                if (reportObject.getString("SzjdBzQz01")!=null){
                                    SzjdBzQz01 szjdBzQz01=JSON.parseObject(reportObject.getString("SzjdBzQz01"),SzjdBzQz01.class);
                                     return;
                                }
                                if (reportObject.getString("SzjdBzQz02")!=null){
                                    SzjdBzQz02 szjdBzQz02=JSON.parseObject(reportObject.getString("SzjdBzQz02"),SzjdBzQz02.class);
                                 return;
                                }
                                if (reportObject.getString("SzjdBzQz04")!=null){
                                    SzjdBzQz04 szjdBzQz04=JSON.parseObject(reportObject.getString("SzjdBzQz04"),SzjdBzQz04.class);
                                    return;
                                }
                                if (reportObject.getString("SzjdBzQz05")!=null){
                                    SzjdBzQz05 szjdBzQz05=JSON.parseObject(reportObject.getString("SzjdBzQz05"),SzjdBzQz05.class);
                                    return;
                                }
                                if (reportObject.getString("SzjdBzQz15")!=null){

                                   szjdBzQz15=JSON.parseObject(reportObject.getString("SzjdBzQz15"),SzjdBzQz15.class);
                                    Log.i(TAG,"获取report"+szjdBzQz15.getApprovalPerson());
                                    getRange();
                                    makeFile();
                                    readAndWrite();
                                    //ReadFile.readff("file:///"+htmlPath,filecode,szjdBzQz15);
                                    view.loadUrl("file:///"+htmlPath);
                                    view.getSettings().setJavaScriptEnabled(true);
                                    view.setWebViewClient(new WebViewClient());
                                    return;
                                }
                                if (reportObject.getString("SzjdBzQz19")!=null){
                                    SzjdBzQz19 szjdBzQz19=JSON.parseObject(reportObject.getString("SzjdBzQz19"),SzjdBzQz19.class);
                                    return;
                                }
                                if (reportObject.getString("SzjdBzQz22")!=null){
                                    SzjdBzQz22 szjdBzQz22=JSON.parseObject(reportObject.getString("SzjdBzQz22"),SzjdBzQz22.class);

                                    return;
                                }
                            }else if (response.trim().equals("暂时没有报告")){

                                Toast.makeText(ReportActivity.this,"暂时没有报告，请稍后再查看",Toast.LENGTH_SHORT).show();
                            }else if (response.trim().equals("获取失败！")){
                                Toast.makeText(ReportActivity.this,"获取失败,请检查网络稍后重试",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Object tag, Throwable e) {
                            Toast.makeText(ReportActivity.this, "获取失败" + e, Toast.LENGTH_SHORT).show();
                            //refreshLayout.setRefreshing(false);

                        }

                        @Override
                        public void onCancel(Object tag, Throwable e) {
                            Toast.makeText(ReportActivity.this, "获取失败" + e, Toast.LENGTH_SHORT).show();
                            //refreshLayout.setRefreshing(false);
                        }
                    });



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }




    public void makeFile(){

        String sdStateString = android.os.Environment.getExternalStorageState();

        if(sdStateString.equals(android.os.Environment.MEDIA_MOUNTED)){
            try{
                File sdFile = android.os.Environment.getExternalStorageDirectory();

//                String path = sdFile.getAbsolutePath() + File.separator + "xiao";
                String path = sdFile.getAbsolutePath() ;
                String temp = path + File.separator + "my.html";

                File dirFile = new File(path);
                if(!dirFile.exists()){
                    dirFile.mkdir();
                }

                File myFile = new File(path + File.separator + "my.html");

                if(!myFile.exists()){
                    myFile.createNewFile();
                }

                htmlPath = myFile.getAbsolutePath();
            }
            catch(Exception e){

            }
        }
    }

    public void makePictureFile(){
        String sdString = android.os.Environment.getExternalStorageState();

        if(sdString.equals(android.os.Environment.MEDIA_MOUNTED)){
            try{
                File picFile = android.os.Environment.getExternalStorageDirectory();

                String picPath = picFile.getAbsolutePath() + File.separator + "xiao";

                File picDirFile = new File(picPath);

                if(!picDirFile.exists()){
                    picDirFile.mkdir();
                }
                File pictureFile = new File(picPath + File.separator + presentPicture + ".jpg");

                if(!pictureFile.exists()){
                    pictureFile.createNewFile();
                }

                picturePath = pictureFile.getAbsolutePath();

            }
            catch(Exception e){
                System.out.println("PictureFile Catch Exception");
            }
        }
    }

    public void onDestroy(){
        super.onDestroy();
    }


    public void readAndWrite(){

        try{
            myFile = new File(htmlPath);
            output = new FileOutputStream(myFile);
            String head = "<html><body>";
            String tagBegin = "<p>";
            String tagEnd = "</p>";


            output.write(head.getBytes());

            int numParagraphs = range.numParagraphs();

            for(int i = 0; i < numParagraphs; i++){

                //Log.i("Test","我是第几个自然段："+i);
                //Paragraph ppr=range.getParagraph(i);
                //Paragraph p =  CreateReport.MakeReport(filecode,ppr,szjdBzQz15) ;

                Paragraph p = range.getParagraph(i);

                if(p.isInTable()){
                    int temp = i;
                    if(tableIterator.hasNext()){
                        String tableBegin = "<table style=\"border-collapse:collapse\" border=1 bordercolor=\"black\">";
                        String tableEnd = "</table>";
                        String rowBegin = "<tr>";
                        String rowEnd = "</tr>";
                        String colBegin = "<td>";
                        String colEnd = "</td>";

                        Table table = tableIterator.next();


                        output.write(tableBegin.getBytes());

                        int rows = table.numRows();

                        for( int r = 0; r < rows; r++){
                            output.write(rowBegin.getBytes());
                            TableRow row = table.getRow(r);
                            int cols = row.numCells();
                            int rowNumParagraphs = row.numParagraphs();
                            int colsNumParagraphs = 0;
                            for( int c = 0; c < cols; c++){
                                output.write(colBegin.getBytes());
                                TableCell cell = row.getCell(c);
                                int max = temp + cell.numParagraphs();
                                colsNumParagraphs = colsNumParagraphs + cell.numParagraphs();
                                for(int cp = temp; cp < max; cp++){
                                    Paragraph p1 = range.getParagraph(cp);
                                    output.write(tagBegin.getBytes());
                                    writeParagraphContent(p1);
                                    output.write(tagEnd.getBytes());
                                    temp++;
                                }
                                output.write(colEnd.getBytes());
                            }
                            int max1 = temp + rowNumParagraphs;
                            for(int m = temp + colsNumParagraphs; m < max1; m++){
                                Paragraph p2 = range.getParagraph(m);
                                temp++;
                            }
                            output.write(rowEnd.getBytes());
                        }
                        output.write(tableEnd.getBytes());
                    }
                    i = temp;
                }
                else{
                    output.write(tagBegin.getBytes());
                    writeParagraphContent(p);
                    output.write(tagEnd.getBytes());
                }
            }

            String end = "</body></html>";
            output.write(end.getBytes());
            output.close();
        }
        catch(Exception e){
            System.out.println("readAndWrite Exception");
        }
    }


    public void writeParagraphContent(Paragraph paragraph){

        //Paragraph p = CreateReport.MakeReport(filecode,paragraph,szjdBzQz15) ;

        Paragraph p = paragraph ;



        int pnumCharacterRuns = p.numCharacterRuns();

        for( int j = 0; j < pnumCharacterRuns; j++){

            CharacterRun run = p.getCharacterRun(j);

            if(run.getPicOffset() == 0 || run.getPicOffset() >= 1000){
                if(presentPicture < pictures.size()){
                    writePicture();
                }
            }
            else{
                try{
                    String textvv = run.text();
                    //String text = run.text();
                    //String text=CreateReport.MakeReport(filecode,textvv,szjdBzQz15);
                    String text= ReportSzjdBzQz15.ReportSzjdBzQz15(szjdBzQz15,textvv);
                    //String text=textvv.replace("###006","搞什么鬼");

                    Log.i("contentttt=======",text);
                    if(text.length() >= 2 && pnumCharacterRuns < 2){
                        output.write(text.getBytes());
                    }
                    else{
                        int size = run.getFontSize();
                        int color = run.getColor();
                        String fontSizeBegin = "<font size=\"" + decideSize(size) + "\">";
                        String fontColorBegin = "<font color=\"" + decideColor(color) + "\">";
                        String fontEnd = "</font>";
                        String boldBegin = "<b>";
                        String boldEnd = "</b>";
                        String islaBegin = "<i>";
                        String islaEnd = "</i>";

                        output.write(fontSizeBegin.getBytes());
                        output.write(fontColorBegin.getBytes());

                        if(run.isBold()){
                            output.write(boldBegin.getBytes());
                        }
                        if(run.isItalic()){
                            output.write(islaBegin.getBytes());
                        }

                        //text.replace("###004","我是被替换的");
                        //String text =CreateReport.MakeReport(filecode,run.text(),szjdBzQz15) ;;

                        output.write(text.getBytes());


                        if(run.isBold()){
                            output.write(boldEnd.getBytes());
                        }
                        if(run.isItalic()){
                            output.write(islaEnd.getBytes());
                        }
                        output.write(fontEnd.getBytes());
                        output.write(fontEnd.getBytes());
                    }
                }
                catch(Exception e){
                    System.out.println("Write File Exception");
                }
            }
        }
    }


    public void writePicture(){
        Picture picture = (Picture)pictures.get(presentPicture);

        byte[] pictureBytes = picture.getContent();

        Bitmap bitmap = BitmapFactory.decodeByteArray(pictureBytes, 0, pictureBytes.length);

        makePictureFile();
        presentPicture++;

        File myPicture = new File(picturePath);

        try{

            FileOutputStream outputPicture = new FileOutputStream(myPicture);

            outputPicture.write(pictureBytes);

            outputPicture.close();
        }
        catch(Exception e){
            System.out.println("outputPicture Exception");
        }

        String imageString = "<img src=\"" + picturePath + "\"";

        if(bitmap.getWidth() > screenWidth){
            imageString = imageString + " " + "width=\"" + screenWidth + "\"";
        }
        imageString = imageString + ">";

        try{
            output.write(imageString.getBytes());
        }
        catch(Exception e){
            System.out.println("output Exception");
        }
    }


    public int decideSize(int size){

        if(size >= 1 && size <= 8){
            return 1;
        }
        if(size >= 9 && size <= 11){
            return 2;
        }
        if(size >= 12 && size <= 14){
            return 3;
        }
        if(size >= 15 && size <= 19){
            return 4;
        }
        if(size >= 20 && size <= 29){
            return 5;
        }
        if(size >= 30 && size <= 39){
            return 6;
        }
        if(size >= 40){
            return 7;
        }
        return 3;
    }



    private String decideColor(int a){
        int color = a;
        switch(color){
            case 1:
                return "#000000";
            case 2:
                return "#0000FF";
            case 3:
            case 4:
                return "#00FF00";
            case 5:
            case 6:
                return "#FF0000";
            case 7:
                return "#FFFF00";
            case 8:
                return "#FFFFFF";
            case 9:
                return "#CCCCCC";
            case 10:
            case 11:
                return "#00FF00";
            case 12:
                return "#080808";
            case 13:
            case 14:
                return "#FFFF00";
            case 15:
                return "#CCCCCC";
            case 16:
                return "#080808";
            default:
                return "#000000";
        }
    }

    private void getRange(){
        FileInputStream in = null;
        POIFSFileSystem pfs = null;
        try{
            in = new FileInputStream(nameStr);
            pfs = new POIFSFileSystem(in);
            hwpf = new HWPFDocument(pfs);
        }
        catch(Exception e){

        }
        range = hwpf.getRange();

        pictures = hwpf.getPicturesTable().getAllPictures();

        tableIterator = new TableIterator(range);

    }


}
