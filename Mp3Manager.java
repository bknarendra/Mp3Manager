/*
Narendra Rajput
My email : bknarendra2008@gmail.com
My blog : narenonit.blogspot.com
*/
import java.io.*;
import com.mpatric.mp3agic.*;
public class Mp3Manager{
	public static File dir=null,other=null;
	public static int album=1,artist=2,type;
	public static int tot=0;
	public static void main(String args[]) throws Exception {
		if(args.length!=2){
			System.out.println("Usage : run directory_name type_to_categorise_and_store(For album-1/For artist-2)");
			return;
		}
		dir=new File(args[0]);
		if(!dir.exists()) if(!dir.mkdirs()){System.out.println("Error occurred in generating music directory so exiting program");return;}
		other=new File(new String(dir.getCanonicalPath()+"\\"+"other"));
		if(!other.exists()) if(!other.mkdirs()){System.out.println("Error occurred in generating other directory so exiting program");return;}
		type=Integer.parseInt(args[1]);
		File[]roots=File.listRoots();
		for(File root:roots){
			process(root);
			System.gc();
		}
	}
	public static void process(File f) throws Exception{
		if(f.isDirectory()){
			String arr[]=f.list();
			if(arr!=null)
				for(int i=0;i<arr.length;i++)
					process(new File(f,arr[i]));
		}
		if(f.isFile()&&(f.getName().endsWith(".mp3")||f.getName().endsWith(".MP3")||f.getName().endsWith(".Mp3")||f.getName().endsWith(".mP3"))){
			Mp3File mp3file;
			File file;
			try{
			mp3file=new Mp3File(f.getCanonicalPath());
			}catch(Exception e){return;}
			String title="",artistoralbum="",con1="",con="";
			if(mp3file.hasId3v1Tag()) {
        		ID3v1 tag=mp3file.getId3v1Tag();
				title=tag.getTitle();
				title=title.replaceAll("[^a-zA-Z0-9,/\\.'\\\" ]","");
				if(type==album)	artistoralbum=tag.getAlbum();
				else if(type==artist) artistoralbum=tag.getArtist();
				artistoralbum=artistoralbum.replaceAll("[^a-zA-Z0-9,/\\.'\\\"\\- ]","");
				artistoralbum=artistoralbum.trim();
				if(artistoralbum==null||artistoralbum.length()==0) con1=other.getCanonicalPath();
				else{
					con1=dir.getCanonicalPath()+"\\"+artistoralbum;
					File d=new File(con1);
					if(!d.exists()) if(!d.mkdirs()){System.out.println("Error occurred in generating the artistoralbum directory so exiting program");return;}
				}
				con=con1+"\\"+title+".mp3";
				file=new File(con);
			}
			else file=new File(new String(other.getCanonicalPath()+"\\"+f.getName()));
			if(f.equals(file)) {System.out.println("Already exists");return;}
			if(file.exists()) return;
			FileInputStream inStream=new FileInputStream(f);
    		FileOutputStream outStream=new FileOutputStream(file);
			byte[] buffer=new byte[1024];
    		int length;
    		while((length=inStream.read(buffer))>0) outStream.write(buffer,0,length);
			System.out.println("Moved "+f.getCanonicalPath()+" to "+file.getCanonicalPath());
    		inStream.close();
    		outStream.close();
			f.delete();
		}
	}
}