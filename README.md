# 요구사항 정의
## Job - 1
### 기능: 
파일로부터 데이터를 읽어서 DB에 적재
#### 세부 내용:  
파일은 매일 새롭게 생성된다.  
매일 정해진 시간에 파일을 로드하고 데이터를 DB에 업데이트 한다.  
이미 처리한 파일은 다시 읽지 않도록 한다.  
## Job – 2
### 기능:
DB로부터 데이터를 읽어와 API 서버와 통신한다.  
Multi Thread로 작업하도록 한다.  
### 세부 내용:
Partitioning 기능을 통한 멀티 Thread 구조로 Chunk 기반 프로세스를 구현  
제품의 유형에 따라서 서로 다른 API 통신을 하도록 구성한다(ClassifierCompositerItemWriter)  
API 서버는 3개로 구성하여 요청을 처리한다.    
제품 내용과 API 통신 결과를 각 파일로 저장한다(FlatFileWriter 상속)
## Scheduler
### 기능:
시간을 설정하면 프로그램을 가동시킨다
### 내용: 
정해진 시간에 주기적으로 Job1와 Job2를 실행시킨다.  
Quatz 오픈 소스를 활용한다.

# 프로세스 흐픔도
![image](https://github.com/user-attachments/assets/2ae38e5b-bc39-46cd-961b-1016c145e335)

# JobStep 상세
![image](https://github.com/user-attachments/assets/accf6aa3-9c07-4f8e-9af7-b7e01d53d9e8)

# ApiSlaveStep 상세
![image](https://github.com/user-attachments/assets/d960d37a-2e6f-47fc-95c1-d36dad03de76)
