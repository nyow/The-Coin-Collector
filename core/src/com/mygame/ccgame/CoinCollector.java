package com.mygame.ccgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class CoinCollector extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] man;
	int manstate=0,pause=0;
	float gravity=0.2f;
	float velocity=0;
	int manY=0;

	ArrayList<Integer> coinx = new ArrayList<Integer>();
	ArrayList<Integer> coiny = new ArrayList<Integer>();
	Texture coin;
	Random random;
	int coincount;

	ArrayList<Integer> bombx = new ArrayList<Integer>();
	ArrayList<Integer> bomby = new ArrayList<Integer>();
	Texture bomb;
	int bombcount;

	ArrayList<Rectangle> coinRect= new ArrayList<>();
	ArrayList<Rectangle> bombRect= new ArrayList<>();

	Rectangle manReact;
	int score=0;

	BitmapFont font;

	int gamestate=0;
	Texture dizzy;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		man= new Texture[4];
		man[0]= new Texture("frame-1.png");
		man[1]= new Texture("frame-2.png");
		man[2]= new Texture("frame-3.png");
		man[3]= new Texture("frame-4.png");
		coin=new Texture("coin.png");
		bomb=new Texture("bomb.png");
		dizzy=new Texture("dizzy-1.png");
		random= new Random();
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
	}

	public void makeCoin()
	{
		float height =random.nextFloat()*Gdx.graphics.getHeight();
		coiny.add((int)height);
		coinx.add(Gdx.graphics.getWidth());
	}
	public void makeBomb()
	{
		float height =random.nextFloat()*Gdx.graphics.getHeight();
		bomby.add((int)height);
		bombx.add(Gdx.graphics.getWidth());
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		if(gamestate==1) ///game is runnin
		{

			// hi
			if(bombcount <250) {
				bombcount++;
			}
			else {
				bombcount=0;
				makeBomb();
			}
			bombRect.clear();

			for (int i=0; i<bombx.size();i++){
				batch.draw(bomb,bombx.get(i),bomby.get(i));
				bombx.set(i,bombx.get(i)-8);
				bombRect.add(new Rectangle(bombx.get(i),bomby.get(i),bomb.getWidth(),bomb.getHeight()));
			}

			if(coincount <150)
			{
				coincount++;
			}
			else
			{
				coincount=0;
				makeCoin();
			}

			coinRect.clear();
			for (int i=0; i<coinx.size();i++){
				batch.draw(coin,coinx.get(i),coiny.get(i));
				coinx.set(i,coinx.get(i)-4);
				coinRect.add(new Rectangle(coinx.get(i),coiny.get(i),coin.getWidth(),coin.getHeight()));

			}

			if (Gdx.input.justTouched())
			{
				velocity=-10;
			}
			if(pause<8)
			{
				pause++;
			}
			else
			{
				pause=0;
				if(manstate<3)
				{
					manstate++;
				}
				else {
					manstate=0;
				}
			}
			if(manY<=0)
			{
				manY=0;
			}
			if(manY>= Gdx.graphics.getHeight() - man[manstate].getHeight()/2)
			{
				manY=Gdx.graphics.getHeight()- man[manstate].getHeight()/2;
			}
			velocity+=gravity;
			manY -=velocity;


			//,,,,,,,
		}
		else if (gamestate ==0)// abt to start
		{
			if (Gdx.input.justTouched())//restart
			{
				gamestate=1;
			}
		}
		else if (gamestate ==2)//gameover
		{
			if (Gdx.input.justTouched())//restart
			{
				gamestate=1;
				manY= Gdx.graphics.getHeight()/2;
				score=0;
				velocity=0;
				coinx.clear();
				coiny.clear();
				coinRect.clear();
				coincount=0;
				bombx.clear();
				bomby.clear();
				bombRect.clear();
				bombcount=0;
			}

		}


		if(gamestate==2){
			batch.draw(dizzy,Gdx.graphics.getWidth()/2 - man[manstate].getWidth()/2,manY);
		}
		else
		{
			batch.draw(man[manstate],Gdx.graphics.getWidth()/2 - man[manstate].getWidth()/2,manY );

		}





		manReact=new Rectangle(Gdx.graphics.getWidth()/2 - man[manstate].getWidth()/2,manY, man[manstate].getWidth(), man[manstate].getHeight() );

		for( int i=0;i<coinRect.size();i++)
		{
			if(Intersector.overlaps(manReact,coinRect.get(i)))
			{
				score++;
				coinRect.remove(i);
				coinx.remove(i);
				coiny.remove(i);
				break;

				//Gdx.app.log("coin","collision");
			}
		}
		for( int i=0;i<bombRect.size();i++)
		{
			if(Intersector.overlaps(manReact,bombRect.get(i)))
			{
				gamestate=2;
				//Gdx.app.log("bomb","collision");
			}
		}


		 font.draw(batch,String.valueOf(score),100 , 200);


		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
