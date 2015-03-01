import java.util.HashMap;

public class Weather {

	public HashMap<Long, Double> airPressure = new HashMap<Long, Double>();
	public HashMap<Long, Integer> clouds = new HashMap<Long, Integer>();
	public HashMap<Long, Double> temperature = new HashMap<Long, Double>();
	public HashMap<Long, Integer> humidity = new HashMap<Long, Integer>();
	public HashMap<Long, String> windDirection = new HashMap<Long, String>();
	public HashMap<Long, Integer> windForce = new HashMap<Long, Integer>();
	public HashMap<Long, Double> precipitation = new HashMap<Long, Double>();
	public HashMap<Long, Integer> snow = new HashMap<Long, Integer>();

	private int riverkm;
	private String location;

	public Weather(String location, int riverkm) {
		this.riverkm = riverkm;
		this.location = location;
	}

	public int getRiverKm() {
		return this.riverkm;
	}

	public String getLocation() {
		return this.location;
	}
}
