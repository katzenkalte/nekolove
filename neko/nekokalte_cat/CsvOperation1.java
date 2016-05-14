package neko.neko.nekokalte_cat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import android.os.Environment;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

//CSV関連の処理クラス
public class CsvOperation1 {

	TblLovecatRecord csvRec1;

	//SDカードのパス
	String pathForCatdata;
	File fileForCatdata;
	String[][] dataForCatdata;


	//コンストラクタ
	public CsvOperation1() {
		pathForCatdata = Environment.getExternalStorageDirectory() + "/catData.csv";
		fileForCatdata = new File(pathForCatdata);

	}

	public void creatCsv() {
		//csvファイルの１行目
		String[] title = {"catID_id","sexID","ageUnitID", "weightUnitID", "name",
				"sex", "age", "ageUnit", "weight", "weightUnit","hairColor", "facePhoto",
				"thumnail"};

 		try {
	    	// create writer
    		FileOutputStream output = new FileOutputStream(pathForCatdata);
    		OutputStreamWriter owriter=new OutputStreamWriter(output, "SJIS");
    		CSVWriter writer = new CSVWriter(owriter);

    		// write line
        	writer.writeNext(title);


        	// close
			writer.close();
			owriter.close();
			output.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//dataは１行分のデータ
	public void writeCsv(String[] data) {
 		try {
	    	// create writer
    		FileOutputStream output = new FileOutputStream(pathForCatdata, true);
    		OutputStreamWriter owriter=new OutputStreamWriter(output, "SJIS");
    		CSVWriter writer = new CSVWriter(owriter);

    		// write line
        	writer.writeNext(data);

        	// close
			writer.close();
			owriter.close();
			output.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//CSVファイルを読み込む
	public ArrayList<TblLovecatRecord> readCsv() {
		//csvファイル内のデータを入れる
		ArrayList<TblLovecatRecord> ar = new ArrayList<TblLovecatRecord>();
		//ファイルがなかったら配列にnullを入れる
		if (!fileForCatdata.exists()) {
			ar = null;
			return ar;

		} else {
			try {
				// create reader
				InputStream input = new FileInputStream(pathForCatdata);
				InputStreamReader ireader=new InputStreamReader(input, "SJIS");
				CSVReader reader = new CSVReader(ireader,',','"',1);
				String[] temp;

				//csvファイル内のデータ読み込み
				while((temp = reader.readNext()) != null) {
					csvRec1 = new TblLovecatRecord(0, 0, 0, 0, temp[0], temp[1], pathForCatdata, pathForCatdata, pathForCatdata, pathForCatdata, pathForCatdata, null, null);
					ar.add(csvRec1);
				}
				reader.close();
				ireader.close();
				input.close();

			} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return ar;
		}

	}
}
