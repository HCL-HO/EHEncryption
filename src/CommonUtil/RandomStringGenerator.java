package CommonUtil;

public class RandomStringGenerator {
	private String randomString =
			"iuagsdivjkxzcbvuiwoery8237450623958602947897(*^&*%^@#%$(*Dsjafjkg9y98#!($^hfdjkbvk";
	private int keyLength = 16; 
	private int debug = 0;
	
	public String getRandomString(int len){
		String random = "";
		while (random.length() < keyLength){
			int randomIndex = (int) (Math.random() * (randomString.length()-1));
			random += randomString.substring(randomIndex, randomIndex+1);
		}
		if(debug == 1){
			System.out.println("Random String : "+ random);

		}
		return random;
	}
	
	public void setRandomStringSource(String string){
		this.randomString = string;
	}
	
	public void startDebugMode(){
		this.debug = 1;
	}
}
