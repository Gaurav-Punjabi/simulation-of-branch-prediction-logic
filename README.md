# Simulation-of-Branch-Prediction-Logic
  This is the simulation for calculating the accuracy of the Branch Prediction logic of Pentium Processors.
  So the program takes a C/C++ program as an input and displays the percentage of the sucess of the predication algorithm.
  This program is developed on Java.

## Need of the Branch Prediction Logic
  * As we know the micro-processors use piplelining to increase its performance.
  But one of the major disadvantage of it is the branching statements(.i.e. conditional statements) as it would fail the 
  pipelining and the processor needs to flush all the pre-fetched instructions.
  
  * To avoid this Pentium Processor came up with Branch Prediction Algorithm.

## How the Branch Predication Algorithm Works ?
  Basically the general idea of this algorithm is to store a History Bit(of size 2-Bit) for each and every conditional           statements.
  
  * Structure of the History Bit
    The History Bit has in total 4 states as shown below
    
                           -----------------------------------------------------------------
                          | Strongly False  | Lightly False | Lightly True  | Strongly True |
                          |     0 0         |       0 1     |     1 0       |     1 1       |
                           -----------------------------------------------------------------
     NOTE :  By default the history bit is set to strongly False.
   
 * Actual Working : 
   If the values are 00 or 01 then the history bit predicts that the respective branch wont be taken otherwise it predicts        that the respective branch would be taken.
   
   So whenever a condition arises we check the actual result and the predicted result.
   If the prediction was correct then the state proceeds towards "Strongly True" else it proceeds towards "Strongly False".
   When the state is already at "Strongly False" and the prediction was false then it does not change its state until the 
   prediction becomes true same goes for "Strongly True".

## Instructions on how to use it
  * It is pretty simple to test the branch prediction of a given program.You just need to pass the path of the Program that you want to test as command line arguments.
  * Below is the syntax to run the application (NOTE First you need to compile the program using javac).
  
            java PredictionCalculator [PATH TO THE C/C++ CODE]
