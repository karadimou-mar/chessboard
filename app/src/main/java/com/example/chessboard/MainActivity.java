package com.example.chessboard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private Resources mResources;
    private int N = 8;
    private ImageView mStart = null;
    private ImageView mTarget = null;
    private Button btnGo, btnReset;
    private int mStartingPositionX, mStartingPositionY, mTargetPositionX, mTargetPositionY;
    private static List<Move> moves = new ArrayList<>();
    private String message="";

    static {
        moves.add(new Move(1, -2));
        moves.add(new Move(2, -1));
        moves.add(new Move(2, 1));
        moves.add(new Move(1, 2));
        moves.add(new Move(-1, 2));
        moves.add(new Move(-2, 1));
        moves.add(new Move(-2, -1));
        moves.add(new Move(-1, -2));
    }

    private List<Path> validPaths = new ArrayList<>();
    private List<String> printList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFields();



        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPaths(3, new Position(mStartingPositionX, mStartingPositionY),
                        new Position(mTargetPositionX, mTargetPositionY));
//                printList.clear();

                StringBuilder msg = new StringBuilder();
                for (Path path : validPaths) {
                    //int count = 0;

                    for (int i = 0; i < path.getPositions().size(); i++) {

                        int X = path.getPositions().get(i).getX();
                        int Y = path.getPositions().get(i).getY();
                        char charX = convertXtoChar(X);
                        String combo = Character.toString(charX) + Y;
                        msg.append(combo);
                        if(i == path.getPositions().size()-1){
                            msg.append("\n");
                        }else{
                            msg.append("\u2192");
                        }

//                        printList.add(msg.toString());

                        //count++;

                        Log.i("paths", "# " + i + " Position : " + combo + " " + path.getMoves().get(i).toString());
                        //Log.i("paths", " " + path.getPositions().get(i).toString() + " " + path.getMoves().get(i).toString());
                    }
                    message = message + msg.toString();
                    msg.setLength(0);
                }


                if (validPaths.isEmpty()) {
                    pathsNotFoundAlertDialog(v);
                }
                if (!validPaths.isEmpty()) {
                    displayPathsDialog(v);
                }

            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });


    }

    /**
     * Finds the valid paths BFS style.
     *
     * @param steps max moves of the algorithm
     * @param startingPosition starting position of the knight
     * @param targetPosition ending position of the knight
     */
    public void findPaths(int steps, Position startingPosition, Position targetPosition) {

        Vector<Path> q = new Vector<>();
        List<Position> pathPositions = new ArrayList<>();
        pathPositions.add(startingPosition);
        List<Move> pathMoves = new ArrayList<>();
        pathMoves.add(new Move(0, 0));
        Path startingPath = new Path(pathPositions, pathMoves);
        q.add(startingPath);
        int step = 0;
        while (!q.isEmpty()) {
            Path currentPath = q.firstElement();
            step = currentPath.getPositions().size() - 1;
            q.remove(0);

            if (step < steps) {
                for (int i = 0; i < moves.size(); i++) {
                    int dx = moves.get(i).getDx();
                    int dy = moves.get(i).getDy();
                    if (currentPath.getPositions().get(step).isValidMove(dx, dy, N)) {
                        Position currentPosition = currentPath.getPositions().get(step);
                        Position newPosition = new Position(currentPosition.getX() + dx, currentPosition.getY() + dy);
                        Path newPath = new Path(currentPath);
                        newPath.addPosition(newPosition);
                        newPath.addMove(new Move(dx, dy));
                        if (!newPosition.equals(targetPosition)) {
                            q.add(newPath);
                        } else {
                            validPaths.add(newPath);
                        }
                    }
                }

            }
        }

    }

    /**
     * Converts X square coordinate to algebraic
     *
     * @param c X coordinate
     * @return
     */
    public char convertXtoChar(int c) {
        switch (c) {
            case 1:
                return 'a';
            case 2:
                return 'b';
            case 3:
                return 'c';
            case 4:
                return 'd';
            case 5:
                return 'e';
            case 6:
                return 'f';
            case 7:
                return 'g';
            case 8:
                return 'h';
            default:
                return 'z';
        }
    }


    private void initFields() {
        mContext = getApplicationContext();
        mResources = getResources();
        btnGo = (Button) findViewById(R.id.btnGo);
        btnReset = (Button) findViewById(R.id.btnReset);
    }

//    public boolean isValid(int row, int col, int N) {
//        if (row >= 0 && col >= 0 && row < N && col < N) {
//            return true;
//        }
//        return false;
//    }


    /**
     * Finds X,Y coordinates on screen
     */
    public void findPosition() {
        int coordinatesStart = Integer.parseInt((String) mStart.getTag());
        mStartingPositionX = coordinatesStart / 10;
        mStartingPositionY = coordinatesStart % 10;
        Log.i("CoordinatesStart ", "[" + mStartingPositionX + "]" + "[" + mStartingPositionY + "]");

        int coordinatesTarget = Integer.parseInt((String) mTarget.getTag());
        mTargetPositionX = coordinatesTarget / 10;
        mTargetPositionY = coordinatesTarget % 10;
        Log.i("CoordinatesTarget ", "[" + mTargetPositionX + "]" + "[" + mTargetPositionY + "]");

    }



    public void tileIsClicked(View view) {

        if (mStart == null) {
            mStart = (ImageView) view;
            mStart.setImageResource(R.drawable.knight);
            //mStart.getLocationOnScreen();
        } else if (mTarget == null) {
            mTarget = (ImageView) view;
            if (mStart.getId() != mTarget.getId()) {
                mTarget.setImageResource(R.drawable.target);
                findPosition();
            } else {
                mTarget = null;
            }
        }

    }

    public void reset() {
        message = "";
        validPaths.clear();
        if (mStart != null && mTarget == null) {
            mStart.setImageResource(0);
            mStart = null;
        }
        if ((mStart != null)) {
            mStart.setImageResource(0);
            mTarget.setImageResource(0);
            mStart = null;
            mTarget = null;
        }
    }

    public void pathsNotFoundAlertDialog(View view) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Oops, no valid path. What about another try?");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        reset();
                        Toast.makeText(MainActivity.this, "Good luck!", Toast.LENGTH_LONG).show();
                    }
                });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Bye!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void displayPathsDialog(View view) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Valid Paths: ");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage("" + message);
        alertDialogBuilder.setPositiveButton("exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reset();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}


