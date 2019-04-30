package stranded.game.state;

import org.lwjgl.opengl.GL11;

import stranded.game.entity.Character;
import stranded.game.map.Block;
import stranded.game.map.Chunk;
import stranded.game.map.CubeMap;
import stranded.geometry.Point3D;
import stranded.graphics.Camera;

public class FirstPersonState implements GameState {

    private static CubeMap map;
    private Character player;

    @Override
    public void init() {
        player = new Character();
        player.setControlledByUser(true);
        player.setMoveSpeedOfStd(1.0);
        player.setJumpSpeedOfStd(1.0);
        player.setWidth(1.35 / Block.SIZE_FEET);
        player.setHeight(5.75 / Block.SIZE_FEET);
        player.setEyeHeight(5.5 / Block.SIZE_FEET);
        player.setDepth(1.35 / Block.SIZE_FEET);
        player.setPos(new Point3D(25.0, 80.0, 25.0));
        map = new CubeMap();
        map.createTerrain();
    }

    @Override
    public void tick(double delta) {
        player.tick(delta, map.blocks);
    }

    @Override
    public void render() {
        if (Camera.solidRendering) {
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
        } else {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
        }
        GL11.glLoadIdentity();
        GL11.glRotated(player.getRot().upDown, 1f, 0f, 0f);
        GL11.glRotated(player.getRot().leftRight, 0f, 1f, 0f);
        GL11.glRotated(player.getRot().twist, 0f, 0f, 1f);
        GL11.glTranslated(-player.getPerspectivePos().x, -player.getPerspectivePos().y, -player.getPerspectivePos().z);

        map.setRenderFrom(new Point3D(player.getPerspectivePos().x / Chunk.SIZE, player.getPerspectivePos().y / Chunk.SIZE, player
                .getPerspectivePos().z / Chunk.SIZE));
        map.render();
    }

    @Override
    public void dispose() {
        map.dispose();
    }
}
