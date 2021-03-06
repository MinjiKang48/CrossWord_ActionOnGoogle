package com.o2o.action.server.app;


import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.Arrays;

public class GameBoard {
    // 보드 배열
    private char Board[][];
    // 정답변수
    private AnswerWord[] answer;
    // 맞춘 정답 리스트
    private  ArrayList<AnswerWord> answerlist;
    // 맞춰야 할 정답 리스트
    private  ArrayList<AnswerWord> restanswerlist;
    //정답 개수
    private  int answercount;
    // 보드판 가로길이
    private  int x;
    // 보드판 세로길이
    private int y;
    // 유저정보
    private UserInfo userinfo;
    // 난이도
    public enum Difficulty{easy, hard, medium}
    // 현재 게임판의 스테이지
    private int stage;
    // 현재 게임판의 난이도
    private Difficulty difficulty;
    //GameInfo
    private GameInfo gameinfo;
    // 생성자

    public GameBoard(Difficulty _difficulty, int _stage)
    {
        // UserInfo를 생성
        userinfo = new UserInfo(1,31,54,3,5000);
        // 매개변수 대입
        stage = _stage;
        difficulty = _difficulty;
        // 보드판 생성
        makeBoard(stage+2,stage+2,3);
    }
    private void printBoard(char[][]board,int x, int y)
    {
        for (int i = 0; i<y; i++)
        {
            for (int j = 0; j<x; j++)
            {
                System.out.print(Board[i][j] + " ");
            }
            System.out.println();
        }
    }
    // 타임시간 가져오기
    public int getTimeLimit()
    {
        return 30;
    }
    // 총 단어 개수 가져오기
    public int getTotalWord()
    {
        return answercount;
    }
    // 보드판 배열 가져오기
    public char[][] getBoard()
    {
        return Board;
    }
    // 정답 DB에서 가져오기
    private void loadAnswer()
    {
        answer = new AnswerWord[x];
     /*   for (int i = 0; i<answercount; i++)
        {*/
            AnswerWord answerWord = new AnswerWord("aaa",new String[]{"힌트입니다."});
        answer[0]=answerWord;
        answerWord = new AnswerWord("ccc",new String[]{"힌트입니다."});
        answer[1]=answerWord;
        answerWord = new AnswerWord("ddd",new String[]{"힌트입니다."});
        answer[2]=answerWord;
        //}
    }
    // 보드판 만들기
    private void makeBoard(int _x, int _y, int _answercount)
    {
        x = _x;
        y = _y;
        answercount = _answercount;
        Board = new char[y][x];
        answerlist = new ArrayList<AnswerWord>();
        restanswerlist = new ArrayList<AnswerWord>();
        // 정답 불러오기
        loadAnswer();
        // 채워야할 정답 리스트에 모든 정답 넣기
        for (int i=0; i<_answercount; i++)
        {
            restanswerlist.add(answer[i]);
        }
        // 보드 구성(정답이 아닌 곳은 b로 구성)
        for (int i = 0; i<y; i++)
        {
            for (int j = 0; j<x; j++)
            {
                Board[i][j] = 'b';
            }
        }

        // 보드 구성(정답인 곳에 정답 넣기)
        for (int i = 0; i<answercount; i++)
        {
            for (int j = 0; j<answer[i].getAnswerLength(); j++)
            {
                Board[i][j] = answer[i].getAnswerChar(j);
            }
        }
        printBoard(Board,x,y);
    }
    // 해당 정답의 힌트 얻기
    private String getHint(AnswerWord _answerWord)
    {
        //만약 힌트가 없으면 "NoHint" 출력
        return _answerWord.useHint();
    }
    // 힌트 메세지 출력
    public String getHintMessage()
    {
        if(restanswerlist.size()==0)
            return "맞춰야 할 단어가 더이상 없습니다.";
        return restanswerlist.get(0).useHint();
    }
    // 정답 시도
    public boolean tryAnswer(String _answer)
    {
        // 정답맞았는지 체크
        for (int i = 0; i<answercount; i++)
        {
            if(answer[i].getAnswer().equals(_answer))
            {
                // 정답 맞았으면 정답리스트에 추가하고 맞춰야할 리스트에서 제거
                answerlist.add(answer[i]);
                if(restanswerlist.size()>0)
                    restanswerlist.remove(answer[i]);
                return  true;
            }
        }
        return false;
    }
    // 결과값 가져오기
    public Result getResult()
    {
        Result result = new Result();
        // 결과 객체에 정답리스트와 맞춰야할답 리스트 복사 후 리턴
        for (AnswerWord i:answerlist) {
            result.answer.add(i.getAnswer());
        }
        for (AnswerWord i:restanswerlist) {
            result.restword.add(i.getAnswer());
        }
        return result;
    }
}
