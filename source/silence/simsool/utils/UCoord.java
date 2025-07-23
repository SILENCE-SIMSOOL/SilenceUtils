package silence.simsool.utils;

public class UCoord {

	public static boolean isWithinBounds(Coord pos1, Coord pos2, double x, double y, double z) {
		return (x >= Math.min(pos1.getX(), pos2.getX()) && x <= Math.max(pos1.getX(), pos2.getX()))
				&& (y >= Math.min(pos1.getY(), pos2.getY()) && y <= Math.max(pos1.getY(), pos2.getY()))
				&& (z >= Math.min(pos1.getZ(), pos2.getZ()) && z <= Math.max(pos1.getZ(), pos2.getZ())
		);
	}

	public static boolean isWithinBounds(Coord pos1, Coord pos2, BlockPos playerPos) {
		double x = playerPos.getX();
		double y = playerPos.getY();
		double z = playerPos.getZ();
		return (x >= Math.min(pos1.getX(), pos2.getX()) && x <= Math.max(pos1.getX(), pos2.getX()))
				&& (y >= Math.min(pos1.getY(), pos2.getY()) && y <= Math.max(pos1.getY(), pos2.getY()))
				&& (z >= Math.min(pos1.getZ(), pos2.getZ()) && z <= Math.max(pos1.getZ(), pos2.getZ())
		);
	}

	public static boolean isWithinBounds2D(double x, double x2, double z, double z2, BlockPos playerPos) {
		return (playerPos.getX() >= Math.min(x, x2) && playerPos.getX() <= Math.max(x, x2))
				&& (playerPos.getY() >= Math.min(z, z2) && playerPos.getY() <= Math.max(z, z2)
		);
	}

	public static class Coord {
		private double x;
		private double y;
		private double z;

		public Coord(double x, double y, double z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public double getX() {
			return x;
		}

		public double getY() {
			return y;
		}

		public double getZ() {
			return z;
		}
	}

	public static class PlayerPos {
		private double x;
		private double y;
		private double z;
		private float yaw;
		private float pitch;

		public PlayerPos(double x, double y, double z, float yaw, float pitch) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.yaw = yaw;
			this.pitch = pitch;
		}

		public double getX() {
			return x;
		}

		public double getY() {
			return y;
		}

		public double getZ() {
			return z;
		}

		public float getYaw() {
			return yaw;
		}

		public float getPitch() {
			return pitch;
		}
	}

}
