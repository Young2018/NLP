package cn.young22.bayes;

/**
* <b>类</b>条件概率计算
*
* <h3>类条件概率</h3>
* P(x<sub>j</sub>|c<sub>j</sub>)=( N(X=x<sub>i</sub>, C=c<sub>j
* </sub>)+1 ) <b>/</b> ( N(C=c<sub>j</sub>)+M+V ) <br>
* 其中，N(X=x<sub>i</sub>, C=c<sub>j</sub>）表示类别c<sub>j</sub>中包含属性x<sub>
* i</sub>的训练文本数量；N(C=c<sub>j</sub>)表示类别c<sub>j</sub>中的训练文本数量；M值用于避免
* N(X=x<sub>i</sub>, C=c<sub>j</sub>）过小所引发的问题；V表示类别的总数。
*
* <h3>条件概率</h3>
* <b>定义</b> 设A, B是两个事件，且P(A)>0 称<br>
* <tt>P(B∣A)=P(AB)/P(A)</tt><br>
* 为在条件A下发生的条件事件B发生的条件概率。

*/

public class ClassConditionalProbability {

	private static TrainingDataManager tdm = new TrainingDataManager();
	private static final double M = 0F;
	
	/**
	 * 计算条件概率
	 * @param x 给定的文本属性
	 * @param c 给定的分类
	 * @return 给定条件下的类条件概率
	 */
	public static double calculatePxc(String x, String c){
		double Pxc = 0F;
		double Nxc = tdm.getCountContainKeyOfClassification(c, x);
		double Nc = tdm.getTrainingFileCountOfClassification(c);
		double V = tdm.getTrainingClassifications().length;
		
		Pxc = (Nxc + 1) / (Nc + M + V);
		return Pxc;
	}
	
}