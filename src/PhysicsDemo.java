import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

class PhysicsDemo {

	public static void main(String[] args) {
		World w = new World(new Vec2(10f, 0f), true);

		FixtureDef fd = new FixtureDef();
		PolygonShape sd = new PolygonShape();
		sd.setAsBox(5.0f, 5.0f);
		fd.shape = sd;

		BodyDef bd = new BodyDef();
		bd.position = new Vec2(0.0f, 0.0f);
		bd.type = BodyType.DYNAMIC;
		fd.friction = 0;
		Body b = w.createBody(bd);
		b.createFixture(fd);
		
		for(int i = 0; i < 10; i++) {
			System.out.println( (b.getPosition().x*1000));
			w.step(0.01f, 1, 1);
		}

	}

}