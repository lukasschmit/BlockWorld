package stranded.game.map;

import org.lwjgl.opengl.GL11;

import stranded.graphics.Art;
import stranded.graphics.Renderable;
import stranded.util.Disposable;

public class Chunk implements Renderable, Disposable {

    public int xPos = 0;
    public int yPos = 0;
    public int zPos = 0;
    public static final int SIZE = 16;
    public Block[][][] blocks;
    public int opaqueDisplayListHandle = -1;
    public int transparentDisplayListHandle = -1;

    public Chunk() {
        blocks = new Block[SIZE][SIZE][SIZE];
    }

    public int getTotalCubes() {
        int cubes = 0;

        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                for (int z = 0; z < SIZE; z++) {
                    if (blocks[x][y][z] != null) {
                        cubes++;
                    }
                }
            }
        }
        return cubes;
    }

    public static Block[][][] getChunkAt(int xOffs, int yOffs, int zOffs,
            Block[][][] blocks) {
        Block[][][] chunk = new Block[SIZE][SIZE][SIZE];

        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                for (int z = 0; z < SIZE; z++) {
                    chunk[x][y][z] = blocks[x + xOffs][y + yOffs][z + zOffs];
                }
            }
        }
        return chunk;
    }

    public void fillData(Block[][][] cubes) {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                System.arraycopy(cubes[x][y], 0, blocks[x][y], 0, SIZE);
            }
        }
    }

    @Override
    public void dispose() {
        GL11.glDeleteLists(opaqueDisplayListHandle, 1);
        opaqueDisplayListHandle = -1;
    }

    @Override
    public void render() {
        if (opaqueDisplayListHandle == -1) {
            updateDisplayLists();
        }
        renderDisplayLists();
    }

    private void renderDisplayLists() {
        GL11.glCallList(opaqueDisplayListHandle);
    }

    private void updateDisplayLists() {
        opaqueDisplayListHandle = GL11.glGenLists(1);
        GL11.glNewList(opaqueDisplayListHandle, GL11.GL_COMPILE);
        GL11.glBegin(GL11.GL_QUADS);

        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                for (int z = 0; z < SIZE; z++) {
                    if (blocks[x][y][z] == null) {
                        continue;
                    }
                    if (blocks[x][y][z].isOpaque()) {
                        Art.blockMap.texX = blocks[x][y][z].getType();

                        // left
                        Art.blockMap.texY = Block.SIDE;
                        if ((blocks[x][y][z].getRenderFaces() | 0b10000000) == blocks[x][y][z]
                                .getRenderFaces()) {
                            GL11.glTexCoord2d(Art.blockMap.bottomLeft().x,
                                    Art.blockMap.bottomLeft().y);
                            GL11.glVertex3f(xPos + x, yPos + y, zPos + z);
                            GL11.glTexCoord2d(Art.blockMap.bottomRight().x,
                                    Art.blockMap.bottomRight().y);
                            GL11.glVertex3f(xPos + x, yPos + y, zPos + z + 1);
                            GL11.glTexCoord2d(Art.blockMap.topRight().x,
                                    Art.blockMap.topRight().y);
                            GL11.glVertex3f(xPos + x, yPos + y + 1, zPos + z
                                    + 1);
                            GL11.glTexCoord2d(Art.blockMap.topLeft().x,
                                    Art.blockMap.topLeft().y);
                            GL11.glVertex3f(xPos + x, yPos + y + 1, zPos + z);
                        }

                        // right
                        Art.blockMap.texY = Block.SIDE;
                        if ((blocks[x][y][z].getRenderFaces() | 0b01000000) == blocks[x][y][z]
                                .getRenderFaces()) {
                            GL11.glTexCoord2d(Art.blockMap.bottomLeft().x,
                                    Art.blockMap.bottomLeft().y);
                            GL11.glVertex3f(xPos + x + 1, yPos + y, zPos + z
                                    + 1);
                            GL11.glTexCoord2d(Art.blockMap.bottomRight().x,
                                    Art.blockMap.bottomRight().y);
                            GL11.glVertex3f(xPos + x + 1, yPos + y, zPos + z);
                            GL11.glTexCoord2d(Art.blockMap.topRight().x,
                                    Art.blockMap.topRight().y);
                            GL11.glVertex3f(xPos + x + 1, yPos + y + 1, zPos
                                    + z);
                            GL11.glTexCoord2d(Art.blockMap.topLeft().x,
                                    Art.blockMap.topLeft().y);
                            GL11.glVertex3f(xPos + x + 1, yPos + y + 1, zPos
                                    + z + 1);
                        }

                        // bottom
                        Art.blockMap.texY = Block.BOTTOM;
                        if ((blocks[x][y][z].getRenderFaces() | 0b00100000) == blocks[x][y][z]
                                .getRenderFaces()) {
                            GL11.glTexCoord2d(Art.blockMap.bottomLeft().x,
                                    Art.blockMap.bottomLeft().y);
                            GL11.glVertex3f(xPos + x, yPos + y, zPos + z);
                            GL11.glTexCoord2d(Art.blockMap.bottomRight().x,
                                    Art.blockMap.bottomRight().y);
                            GL11.glVertex3f(xPos + x + 1, yPos + y, zPos + z);
                            GL11.glTexCoord2d(Art.blockMap.topRight().x,
                                    Art.blockMap.topRight().y);
                            GL11.glVertex3f(xPos + x + 1, yPos + y, zPos + z
                                    + 1);
                            GL11.glTexCoord2d(Art.blockMap.topLeft().x,
                                    Art.blockMap.topLeft().y);
                            GL11.glVertex3f(xPos + x, yPos + y, zPos + z + 1);
                        }

                        // top
                        Art.blockMap.texY = Block.TOP;
                        if ((blocks[x][y][z].getRenderFaces() | 0b00010000) == blocks[x][y][z]
                                .getRenderFaces()) {
                            GL11.glTexCoord2d(Art.blockMap.bottomLeft().x,
                                    Art.blockMap.bottomLeft().y);
                            GL11.glVertex3f(xPos + x, yPos + y + 1, zPos + z
                                    + 1);
                            GL11.glTexCoord2d(Art.blockMap.bottomRight().x,
                                    Art.blockMap.bottomRight().y);
                            GL11.glVertex3f(xPos + x + 1, yPos + y + 1, zPos
                                    + z + 1);
                            GL11.glTexCoord2d(Art.blockMap.topRight().x,
                                    Art.blockMap.topRight().y);
                            GL11.glVertex3f(xPos + x + 1, yPos + y + 1, zPos
                                    + z);
                            GL11.glTexCoord2d(Art.blockMap.topLeft().x,
                                    Art.blockMap.topLeft().y);
                            GL11.glVertex3f(xPos + x, yPos + y + 1, zPos + z);
                        }

                        // back
                        Art.blockMap.texY = Block.SIDE;
                        if ((blocks[x][y][z].getRenderFaces() | 0b00001000) == blocks[x][y][z]
                                .getRenderFaces()) {
                            GL11.glTexCoord2d(Art.blockMap.bottomLeft().x,
                                    Art.blockMap.bottomLeft().y);
                            GL11.glVertex3f(xPos + x + 1, yPos + y, zPos + z);
                            GL11.glTexCoord2d(Art.blockMap.bottomRight().x,
                                    Art.blockMap.bottomRight().y);
                            GL11.glVertex3f(xPos + x, yPos + y, zPos + z);
                            GL11.glTexCoord2d(Art.blockMap.topRight().x,
                                    Art.blockMap.topRight().y);
                            GL11.glVertex3f(xPos + x, yPos + y + 1, zPos + z);
                            GL11.glTexCoord2d(Art.blockMap.topLeft().x,
                                    Art.blockMap.topLeft().y);
                            GL11.glVertex3f(xPos + x + 1, yPos + y + 1, zPos
                                    + z);
                        }

                        // front
                        Art.blockMap.texY = Block.SIDE;
                        if ((blocks[x][y][z].getRenderFaces() | 0b00000100) == blocks[x][y][z]
                                .getRenderFaces()) {
                            GL11.glTexCoord2d(Art.blockMap.bottomLeft().x,
                                    Art.blockMap.bottomLeft().y);
                            GL11.glVertex3f(xPos + x, yPos + y, zPos + z + 1);
                            GL11.glTexCoord2d(Art.blockMap.bottomRight().x,
                                    Art.blockMap.bottomRight().y);
                            GL11.glVertex3f(xPos + x + 1, yPos + y, zPos + z
                                    + 1);
                            GL11.glTexCoord2d(Art.blockMap.topRight().x,
                                    Art.blockMap.topRight().y);
                            GL11.glVertex3f(xPos + x + 1, yPos + y + 1, zPos
                                    + z + 1);
                            GL11.glTexCoord2d(Art.blockMap.topLeft().x,
                                    Art.blockMap.topLeft().y);
                            GL11.glVertex3f(xPos + x, yPos + y + 1, zPos + z
                                    + 1);
                        }
                    }
                }
            }
        }
        GL11.glEnd();
        GL11.glEndList();
    }
}
