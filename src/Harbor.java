public class Harbor {

	private String name;
	private int riverkm;
	private double minLong;
	private double maxLong;
	private double minLat;
	private double maxLat;

	public Harbor(String name, int riverkm, double minLong, double maxLong,
			double minLat, double maxLat) {
		this.name = name;
		this.riverkm = riverkm;
		this.minLong = minLong;
		this.maxLong = maxLong;
		this.minLat = minLat;
		this.maxLat = maxLat;
	}

	@Override
	public String toString() {
		return this.name + "-" + this.riverkm + "-" + this.minLong + "-"
				+ this.maxLong + "-" + this.minLat + "-" + this.maxLat;
	}

	public String getName() {
		return this.name;
	}

	public int getRiverkm() {
		return this.riverkm;
	}

	public double getMinLong() {
		return this.minLong;
	}

	public double getMaxLong() {
		return this.maxLong;
	}

	public double getMinLat() {
		return this.minLat;
	}

	public double getMaxLat() {
		return this.maxLat;
	}
}
