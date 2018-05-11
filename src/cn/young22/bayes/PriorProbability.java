package cn.young22.bayes;

/**
* 先验概率计算
* <h3>先验概率计算</h3>
* P(c<sub>j</sub>)=N(C=c<sub>j</sub>)<b>/</b>N <br>
* 其中，N(C=c<sub>j</sub>)表示类别c<sub>j</sub>中的训练文本数量；
* N表示训练文本集总数量。
*/
public class PriorProbability {

	private static TrainingDataManager tdm = new TrainingDataManager();
	
	public static double calculatePc(String c){
		double priorpb = 0F;
		double Nc = tdm.getTrainingFileCountOfClassification(c);
		double N = tdm.getTrainingFileCount();
		priorpb = Nc / N;
		return priorpb;
	}
	
	
}
