package backend.model.param;

public class ParamPt2D extends Param{

	Pt2D value;
	
	public ParamPt2D(String name, String label, double x, double y){
		pname  = name;
		plabel = label;
		ptype  = PType.Pt2D;
		value  = new Pt2D(x,y);
	}
	
	
	public class Pt2D{
		double x = 0.0;
		double y = 0.0;
		public Pt2D(double px, double py){
			x = px;
			y=py;
		}
	}

}
