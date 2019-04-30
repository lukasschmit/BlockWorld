package stranded.game.map;

import org.lwjgl.opengl.GL11;

import stranded.geometry.Point3D;
import stranded.graphics.Art;
import stranded.graphics.Camera;
import stranded.graphics.Renderable;
import stranded.util.Disposable;
import stranded.util.ToolBox;

public class CubeMap implements Renderable, Disposable {

    public static final int HEIGHT = 128;
    public static final int WIDTH = 256;
    public static final int DEPTH = 256;
    public Chunk[][][] chunks;
    public Block[][][] blocks;
    private Point3D renderFrom;

    public int getTotalCubes() {
        int cubes = 0;

        for (int x = 0; x < blocks.length / Chunk.SIZE; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                for (int z = 0; z < blocks[0][0].length / Chunk.SIZE; z++) {
                    cubes += chunks[x][y][z].getTotalCubes();
                }
            }
        }
        return cubes;
    }

    public void update() {
        for (int x = 0; x < WIDTH; x++) {
            for (int z = 0; z < DEPTH; z++) {
                for (int y = 0; y < HEIGHT; y++) {
                    if (blocks[x][y][z] != null) {
                        byte renderFaces = (byte) 0b11111100;

                        try {
                            if (blocks[x - 1][y][z] != null && blocks[x - 1][y][z].isOpaque()) {
                                renderFaces &= 0b01111111;
                            }
                        } catch (ArrayIndexOutOfBoundsException ex) {
                            renderFaces &= 0b01111111;
                        }
                        try {
                            if (blocks[x + 1][y][z] != null && blocks[x + 1][y][z].isOpaque()) {
                                renderFaces &= 0b10111111;
                            }
                        } catch (ArrayIndexOutOfBoundsException ex) {
                            renderFaces &= 0b10111111;
                        }
                        try {
                            if (blocks[x][y - 1][z] != null && blocks[x][y - 1][z].isOpaque()) {
                                renderFaces &= 0b11011111;
                            }
                        } catch (ArrayIndexOutOfBoundsException ex) {
                            renderFaces &= 0b11011111;
                        }
                        try {
                            if (blocks[x][y + 1][z] != null && blocks[x][y + 1][z].isOpaque()) {
                                renderFaces &= 0b11101111;
                            }
                        } catch (ArrayIndexOutOfBoundsException ex) {
                            renderFaces &= 0b11101111;
                        }
                        try {
                            if (blocks[x][y][z - 1] != null && blocks[x][y][z - 1].isOpaque()) {
                                renderFaces &= 0b11110111;
                            }
                        } catch (ArrayIndexOutOfBoundsException ex) {
                            renderFaces &= 0b11110111;
                        }
                        try {
                            if (blocks[x][y][z + 1] != null && blocks[x][y][z + 1].isOpaque()) {
                                renderFaces &= 0b11111011;
                            }
                        } catch (ArrayIndexOutOfBoundsException ex) {
                            renderFaces &= 0b11111011;
                        }

                        blocks[x][y][z].setRenderFaces(renderFaces);
                    }
                }
            }
        }
        for (int x = 0; x < WIDTH / Chunk.SIZE; x++) {
            for (int y = 0; y < HEIGHT / Chunk.SIZE; y++) {
                for (int z = 0; z < DEPTH / Chunk.SIZE; z++) {
                    chunks[x][y][z] = new Chunk();
                    chunks[x][y][z].opaqueDisplayListHandle = -1;
                    chunks[x][y][z].fillData(Chunk.getChunkAt(x * Chunk.SIZE, y * Chunk.SIZE, z * Chunk.SIZE, blocks));
                    chunks[x][y][z].xPos = x * Chunk.SIZE;
                    chunks[x][y][z].yPos = y * Chunk.SIZE;
                    chunks[x][y][z].zPos = z * Chunk.SIZE;
                    chunks[x][y][z].opaqueDisplayListHandle = -1;
                }
            }
        }
    }

    public void createTerrain() {
        blocks = new Block[WIDTH][HEIGHT][DEPTH];
        TerrainGenerator.perlinHeightMap(blocks);
        chunks = new Chunk[WIDTH / Chunk.SIZE][HEIGHT / Chunk.SIZE][DEPTH / Chunk.SIZE];

        update();
    }

    @Override
    public void dispose() {
        for (int x = 0; x < WIDTH / Chunk.SIZE; x++) {
            for (int y = 0; y < HEIGHT / Chunk.SIZE; y++) {
                for (int z = 0; z < DEPTH / Chunk.SIZE; z++) {
                    chunks[x][y][z].dispose();
                }
            }
        }
    }

    @Override
    public void render() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        Art.blockMap.texture.bind();
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);

        for (int x = 0; x < WIDTH / Chunk.SIZE; x++) {
            for (int y = 0; y < HEIGHT / Chunk.SIZE; y++) {
                for (int z = 0; z < DEPTH / Chunk.SIZE; z++) {
                    if (ToolBox.distBetween(renderFrom, new Point3D(x, y, z)) <= Camera.maxChunkDist || ToolBox.distBetween(renderFrom, new Point3D((x + 1), y, z)) <= Camera.maxChunkDist || ToolBox.distBetween(renderFrom, new Point3D(x, (y + 1), z)) <= Camera.maxChunkDist || ToolBox.distBetween(renderFrom, new Point3D(x, y, (z + 1))) <= Camera.maxChunkDist || ToolBox.distBetween(renderFrom, new Point3D((x + 1), (y + 1), z)) <= Camera.maxChunkDist || ToolBox.distBetween(renderFrom, new Point3D((x + 1), y, (z + 1))) <= Camera.maxChunkDist || ToolBox.distBetween(renderFrom, new Point3D(x, (y + 1), (z + 1))) <= Camera.maxChunkDist || ToolBox.distBetween(renderFrom, new Point3D((x + 1), (y + 1), (z + 1))) <= Camera.maxChunkDist) {

                        chunks[x][y][z].render();
                    }
                }
            }
        }
    }

    public Point3D getRenderFrom() {
        return renderFrom;
    }

    public void setRenderFrom(Point3D renderFrom) {
        this.renderFrom = renderFrom;
    }
}
