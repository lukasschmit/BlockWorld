package stranded.game.map;

import stranded.util.Interpolator;

public class TerrainGenerator {

    private static double[][][] density;
    public static long mapSeed = System.currentTimeMillis();
    public static double horizFrequency = 0.075;
    public static double vertiFrequency = 0.075;
    public static double avgGroundHeight = 24.0;
    public static double maxTerrainHeight = 75.0; // above ground height
    private static Interpolator interpolator = new Interpolator(Interpolator.COSINE);

    public static void perlinHeightMap(Block[][][] map) {
        density = new double[map.length][map[0].length][map[0][0].length];

        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {
                for (int z = 0; z < map[0][0].length; z++) {
                    density[x][y][z] = perlin3DAt(x, y, z);

                    if (y < avgGroundHeight) {
                        density[x][y][z] += (avgGroundHeight - y) / avgGroundHeight;
                    } else {
                        density[x][y][z] += (avgGroundHeight - y) / (maxTerrainHeight);
                    }

                    if (density[x][y][z] > 0.0) {
                        map[x][y][z] = new Block(Block.TYPE_SURFACE);
                    } else {
                        map[x][y][z] = null;
                    }
                }
            }
        }

        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length - 1; y++) {
                for (int z = 0; z < map[0][0].length; z++) {
                    if (map[x][y][z] != null && map[x][y + 1][z] != null) {
                        map[x][y][z].setType(Block.TYPE_ROCKS);
                    }
                }
            }
        }
    }

    private static double perlin3DAt(double x, double y, double z) {
        x *= horizFrequency;
        y *= vertiFrequency;
        z *= horizFrequency;
        int nearX = (int) x;
        double offsX = x - nearX;

        int nearY = (int) y;
        double offsY = y - nearY;

        int nearZ = (int) z;
        double offsZ = z - nearZ;

        double interpolated = -1.0;

        double a = interpolator.interpolate(smoothNoiseAt(nearX, nearY, nearZ), smoothNoiseAt(nearX, nearY + 1, nearZ), offsY);
        double b = interpolator.interpolate(smoothNoiseAt(nearX + 1, nearY, nearZ), smoothNoiseAt(nearX + 1, nearY + 1, nearZ), offsY);
        double c = interpolator.interpolate(smoothNoiseAt(nearX + 1, nearY, nearZ + 1), smoothNoiseAt(nearX + 1, nearY + 1, nearZ + 1), offsY);
        double d = interpolator.interpolate(smoothNoiseAt(nearX, nearY, nearZ + 1), smoothNoiseAt(nearX, nearY + 1, nearZ + 1), offsY);

        double e = interpolator.interpolate(a, d, offsZ);
        double f = interpolator.interpolate(b, c, offsZ);

        interpolated = interpolator.interpolate(e, f, offsX);
        return interpolated;
    }

    private static double smoothNoiseAt(int x, int y, int z) {
        double center = randAt(x, y, z);

        double corners = 0.0;
        corners += randAt(x - 1, y - 1, z - 1);
        corners += randAt(x - 1, y - 1, z + 1);
        corners += randAt(x + 1, y - 1, z - 1);
        corners += randAt(x + 1, y - 1, z + 1);

        corners += randAt(x - 1, y + 1, z - 1);
        corners += randAt(x - 1, y + 1, z + 1);
        corners += randAt(x + 1, y + 1, z - 1);
        corners += randAt(x + 1, y + 1, z + 1);

        corners /= 8.0;

        double edges = 0.0;
        edges += randAt(x - 1, y - 1, z - 1);
        edges += randAt(x - 1, y - 1, z + 1);
        edges += randAt(x + 1, y - 1, z - 1);
        edges += randAt(x + 1, y - 1, z + 1);

        edges += randAt(x - 1, y, z - 1);
        edges += randAt(x - 1, y, z + 1);
        edges += randAt(x + 1, y, z - 1);
        edges += randAt(x + 1, y, z + 1);

        edges += randAt(x - 1, y + 1, z - 1);
        edges += randAt(x - 1, y + 1, z + 1);
        edges += randAt(x + 1, y + 1, z - 1);
        edges += randAt(x + 1, y + 1, z + 1);

        edges /= 12.0;

        double faces = 0.0;
        faces += randAt(x, y - 1, z);
        faces += randAt(x, y, z - 1);
        faces += randAt(x, y, z + 1);
        faces += randAt(x - 1, y, z);
        faces += randAt(x + 1, y, z);
        faces += randAt(x, y + 1, z);

        faces /= 6.0;

        double smoothNoise = (center * 0.6) + (faces * 0.2) + (edges * 0.15) + (corners * 0.5);

        return smoothNoise;
    }

    private static double randAt(int x, int y, int z) {
        double rand;
        long n = x + y + z * 43;
        n = (n << 11) ^ n;
        long nn = (n * n + (n * n * mapSeed) + 1376312589) & 0x7FFFFFFF;
        rand = 1.0 - (nn / 1073741824.0);
        return rand;
    }
}
