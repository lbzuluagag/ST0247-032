
public class Depot extends Station {

	public Depot(int num, String name, double x, double y) {
		super(num, name, x, y, type);
		this.number = num;
		this.name = name;
		this.xpos = x;
		this.ypos = y;
	}
}
