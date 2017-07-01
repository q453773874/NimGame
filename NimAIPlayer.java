/**
 * Created by 45377 on 2017/5/8.
 */
/*
	NimAIPlayer.java

	This class is provided as a skeleton code for the tasks of
	Sections 2.3, 2.4 and 2.5 in Project C. Add code (do NOT delete any) to it
	to finish the tasks.

	Coded by: Jin Huang
	Modified by: Jianzhong Qi, 29/04/2015
*/
    import java.util.Arrays;
    import java.util.Random;

    public class NimAIPlayer extends NimPlayer implements Testable
    {
        // you may further extend a class or implement an interface
        // to accomplish the task in Section 2.3
        private int upperLimit;
        private int stoneLeft;


        NimAIPlayer()
        {
            super();
            super.setType("AIPlayer");
        }

        void setGameInfo(int stoneLeft,int upperLimit)
        {
            this.upperLimit = upperLimit;
            this.stoneLeft = stoneLeft;
        }

        // AI for normal game
        public int removeStone()
        {
            int maxMove = 0;
            Random randomGenerator = new Random();
            for(int i = 0; i*(upperLimit + 1) + 1 < stoneLeft; i++)
            {
                int moveStoneNumber = stoneLeft - i*(upperLimit + 1) - 1;
                if (moveStoneNumber <= upperLimit && moveStoneNumber > 0)
                {
                    if (moveStoneNumber > maxMove)
                    {
                        maxMove = moveStoneNumber;
                    }
                }
            }
            // if cannot find correct move number, just random
            if (maxMove == 0)
            {
                return randomGenerator.nextInt(upperLimit) + 1;
            }
            else
            {
                return maxMove;
            }

        }


        private boolean checkSymmtryOfGameStatus(boolean[] available)
        {
            int lengthOfArray = available.length;
            if (lengthOfArray % 2 == 0)
            {
                for (int i = 0; i < (lengthOfArray / 2 - 1); i++)
                {
                    if (available[i] != available[lengthOfArray -1 - i])
                    {
                        return false;
                    }
                }
            }
            // length is odd
            else
            {
                for (int i = 0; i < (lengthOfArray / 2); i++)
                {
                    if (available[i] != available[lengthOfArray -1 - i])
                    {
                        return false;
                    }
                }
            }
            return true;
        }

        // divided the lastMove from String to array
        private Integer[] dividedLastMove(String lastMove)
        {
            String[] lastMoveDivided =lastMove.split(" ");
            Integer[] lastMoveNumber = new Integer[2];
            lastMoveNumber[0] = Integer.parseInt(lastMoveDivided[0]);
            lastMoveNumber[1] = Integer.parseInt(lastMoveDivided[1]);
            return lastMoveNumber;
        }

        // do a symmetry move
        private String symmetryMove(boolean[] available,String lastMove,int start,int end)
        {
            // start from 1
            int finalPosition = start + end;
            Integer symmetryMove;
            if (dividedLastMove(lastMove)[1].equals(1))
            {
                symmetryMove = finalPosition - dividedLastMove(lastMove)[0];
                return symmetryMove.toString() + " " + dividedLastMove(lastMove)[1];
            }
            else if (dividedLastMove(lastMove)[1].equals(2))
            {
                symmetryMove = finalPosition - dividedLastMove(lastMove)[0] - 1;
                return symmetryMove.toString() + " " + dividedLastMove(lastMove)[1];
            }


            //if just symmetry move can not get a symmetry state
            for (int i = 0; i < available.length - 1; i++)
            {
                Integer symmetry = finalPosition - i - 1;
                if (! available[i] &&( i == available.length - 1 || available[i + 1] )&& available[symmetry])
                {
                    return symmetry.toString() + " 1";
                }
                if (! available[i] && ! available[i + 1] && available[symmetry - 2]&& available[symmetry - 1])
                {
                    symmetry -= 1;
                    return symmetry.toString() + " 2";
                }
            }

            return randomMove(available);
        }

        // if can not find a match case, do a random move
        private String randomMove(boolean[] available)
        {
            String moveOne =
                    "";
            String moveTwo = "";
            // get a random number from 0 to (stone number - 1)
            // try to find a position to make a symmetry status
            for (int i = 0; i < available.length; i++)
            {
                boolean[] availableCopy;
                availableCopy = Arrays.copyOf(available, available.length);
                if ((i == available.length - 1 && available[i]) || available[i])
                {
                    availableCopy[i] = false;
                    Integer position = i;
                    if (checkSymmtryOfGameStatus(availableCopy))
                    {
                        position += 1;
                        moveOne = position.toString() + " 1";
                        return moveOne;
                    }
                }
                // move two stones
                else if (available[i] && available[i + 1]) {
                    availableCopy[i] = false;
                    availableCopy[i + 1] = false;
                    Integer position = i;
                    if (checkSymmtryOfGameStatus(availableCopy)) {
                        position += 1;
                        moveTwo = position.toString() + " 2";
                        return moveTwo;
                    }
                }
            }

            for (int i = 0; i < available.length; i++)
            {
                Integer position = i;
                if ( (i == available.length - 1 && available[i]) || available[i])
                {
                    position += 1;
                    return position.toString() + " 1";
                }
                else if (available[i] && available[i+1])
                {
                    position += 1;
                    return position.toString() + " 2";
                }
            }
            // make program happy
            return "0 0";

        }

        // check whether the array is all available
        private boolean checkAvailable(boolean[] available)
        {
           for (int i =0; i < available.length; i++)
           {
               if (! available[i])
               {
                   return false;
               }
           }
           return true;
        }

        // move the middle stone of the array
        private String moveMiddleOne(boolean[] available,int offset)
        {
            String move = "";
            Integer middleStonePosition =
                    (available.length % 2 == 0) ? (available.length / 2) : (available.length / 2) + 2;
            // stone is even
            if (available.length % 2 == 0)
            {
                middleStonePosition += offset;
                move = middleStonePosition.toString() + " 2";
                return move;
            }
            // stone is odd
            else
            {
                middleStonePosition += offset;
                middleStonePosition -= 1;
                move = middleStonePosition.toString() + " 1";
                return move;
            }
        }

        // restore the last status for the array
        private boolean[] theLastStatus(boolean[] available,String lastMove)
        {
            boolean[] availableCopy = Arrays.copyOf(available,available.length);
            if (dividedLastMove(lastMove)[1].equals(1))
            {
                availableCopy[dividedLastMove(lastMove)[0] - 1] = true;
            }
            else if (dividedLastMove(lastMove)[1].equals(2))
            {
                availableCopy[dividedLastMove(lastMove)[0] - 1] = true;
                availableCopy[dividedLastMove(lastMove)[0]] = true;
            }
            return availableCopy;
        }

        // check whether the move is valid
        private boolean checkInvalidMove(String move,boolean[] available)
        {
            String[] moveDivided = move.split(" ");
            int moveStartPosition = Integer.parseInt(moveDivided[0]);
            int moveEndPosition = Integer.parseInt(moveDivided[1]);
            for (int i = moveStartPosition; i < moveStartPosition + moveEndPosition - 1; i++)
            {
                if (! available[i - 1])
                {
                    return false;
                }
            }
            return true;
        }

        public String advancedMove(boolean[] available, String lastMove) {
            // the implementation of the victory
            // guaranteed strategy designed by you

            String move = "";

            boolean[] removeLeftOne = Arrays.copyOfRange(available, 1, available.length);
            boolean[] removeLeftTwo = Arrays.copyOfRange(available, 2, available.length);
            boolean[] removeRightOne = Arrays.copyOfRange(available, 0, available.length - 1);
            boolean[] removeRightTwo = Arrays.copyOfRange(available, 0, available.length - 2);
            // move first
            if (checkAvailable(available))
            {
                move = moveMiddleOne(available, 0);
            }
            else if (checkAvailable(removeLeftOne))
            {
                move = moveMiddleOne(removeLeftOne, 1);
            }
            else if (checkAvailable(removeLeftTwo))
            {
                move = moveMiddleOne(removeLeftTwo, 2);
            }
            else if (checkAvailable(removeRightOne))
            {
                move = moveMiddleOne(removeRightOne, -1);
            }
            else if (checkAvailable(removeRightTwo))
            {
                move = moveMiddleOne(removeRightTwo, -1);
            }
            // not move first
            else
            {
                boolean[] availableCopy = theLastStatus(available, lastMove);
                removeLeftOne = Arrays.copyOfRange(availableCopy, 1, available.length);
                removeLeftTwo = Arrays.copyOfRange(availableCopy, 2, available.length);
                removeRightOne = Arrays.copyOfRange(availableCopy, 0, available.length - 1);
                removeRightTwo = Arrays.copyOfRange(availableCopy, 0, available.length - 2);

                boolean totalSymmtry = checkSymmtryOfGameStatus(availableCopy);
                boolean removeLeftOneSymmtry = checkSymmtryOfGameStatus(removeLeftOne);
                boolean removeLeftTwoSymmtry = checkSymmtryOfGameStatus(removeLeftTwo);
                boolean removeRightOneSymmtry = checkSymmtryOfGameStatus(removeRightOne);
                boolean removeRightTwoSymmtry = checkSymmtryOfGameStatus(removeRightTwo);



                // just make symmetry with rival
                if (totalSymmtry)
                {
                    move = symmetryMove(available, lastMove,1,available.length);
                    if (! checkInvalidMove(move,available))
                    {
                        move = randomMove(available);
                    }
                    return move;
                }
                else if (removeLeftOneSymmtry)
                {
                    move = symmetryMove(removeLeftOne, lastMove,2,available.length);
                    return move;
                }
                else if (removeLeftTwoSymmtry)
                {
                    move = symmetryMove(removeLeftTwo, lastMove,3,available.length);
                    return move;
                }
                else if (removeRightOneSymmtry)
                {
                    move = symmetryMove(removeRightOne, lastMove, 1,available.length - 1);
                    return move;
                }
                else if (removeRightTwoSymmtry)
                {
                    move = symmetryMove(removeRightTwo, lastMove, 1,available.length - 2);
                    return move;
                }
                else
                {
                    move = randomMove(available);
                }
            }
            // not valid move, then just random
            if ( !checkInvalidMove(move,available))
            {
                move = randomMove(available);
            }
            return move;
        }
    }





