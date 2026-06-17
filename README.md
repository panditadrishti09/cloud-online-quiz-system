# Cloud-Based Online Quiz System with Log Analysis using HDFS

## Overview

Cloud-Based Online Quiz System is a full-stack web application developed using Node.js, Express.js, MongoDB, and Hadoop. The platform allows users to register, attempt quizzes, receive instant results, and track performance. Application logs are stored and analyzed using HDFS and MapReduce, demonstrating practical cloud computing and big data concepts.

---

## Features

* User Registration and Authentication
* Secure Password Hashing
* Online Quiz Management
* Real-Time Score Evaluation
* Leaderboard Generation
* Activity Log Generation
* HDFS-Based Log Storage
* MapReduce-Based Log Analysis
* Responsive Web Interface

---

## Technology Stack

### Frontend

* HTML
* CSS
* JavaScript

### Backend

* Node.js
* Express.js

### Database

* MongoDB

### Big Data Technologies

* Hadoop
* HDFS
* MapReduce

### Tools

* Git
* GitHub
* MongoDB Compass

---

## Cloud Computing Concepts Demonstrated

| Concept   | Implementation                                 |
| --------- | ---------------------------------------------- |
| SaaS      | Quiz platform accessible through a web browser |
| PaaS      | Node.js, Express.js, and MongoDB environment   |
| IaaS      | Deployable on virtual machines such as AWS EC2 |
| HDFS      | Distributed storage of quiz activity logs      |
| MapReduce | Analysis of user activity and score data       |

---

## Project Architecture

```text
User
  ↓
Frontend (HTML, CSS, JavaScript)
  ↓
Node.js + Express Server
  ↓
MongoDB Database
  ↓
Application Logs
  ↓
HDFS Storage
  ↓
MapReduce Analytics
```

---

## Project Structure

```text
cloud-online-quiz-system/
│
├── public/
├── server/
├── database/
├── hadoop/
├── logs/
├── package.json
├── package-lock.json
├── README.md
└── .gitignore
```

---

## Key Functionalities

* Registration and Login System
* Quiz Attempt and Submission
* Automatic Score Calculation
* Performance Tracking
* Leaderboard Management
* Log Generation for User Activities
* Distributed Log Storage using HDFS
* Log Analysis using MapReduce

---

## Installation

### Clone Repository

```bash
git clone https://github.com/panditadrishti09/cloud-online-quiz-system.git
cd cloud-online-quiz-system
```

### Install Dependencies

```bash
npm install
```

### Start Application

```bash
npm start
```

### Open Browser

```text
http://localhost:3000
```

---

## Future Enhancements

* AWS Cloud Deployment
* Advanced Analytics Dashboard
* Role-Based Access Control
* AI-Based Performance Insights
* Email Notifications

---

## Author

**Drishti Pandita**

Third Year Computer Engineering Student
Sinhgad Institute of Technology and Science, Pune
