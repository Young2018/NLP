package cn.young22.bayes;

/**
 * 分类结果
 */
public class ClassifyResult {

	public double probability;		//分类的概率
	public String classification;	//分类名称
	public String origionClass; // 原始分类名称
	public int label; // 分类ID
	
	public ClassifyResult(){
		this.probability = 0;
		this.classification = null;
		this.origionClass = null;
		this.label = 0;
	}
	
	public String toString () {
		return label + "\t" + origionClass + "\t" + classification + "\t" + probability ;
	}


	
	
}
