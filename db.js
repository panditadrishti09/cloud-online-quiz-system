// =============================================================
// DATABASE CONFIGURATION
// Cloud Computing Concept: PaaS (Platform as a Service)
// MongoDB Atlas is a fully managed cloud database service.
// It eliminates the need to manage database infrastructure.
//
// IaaS Concept: If self-hosting, MongoDB would run on AWS EC2
// (Infrastructure as a Service) — raw VM provided by Amazon.
// =============================================================

const mongoose = require('mongoose');

// ---------------------------------------------------------
// OPTION A: MongoDB Atlas (Cloud - Recommended for demo)
// Replace the URI below with your Atlas connection string.
// Get it from: [cloud.mongodb.com](https://cloud.mongodb.com)
// Format: mongodb+srv://<user>:<password>@cluster.mongodb.net/quizdb
//
// OPTION B: Local MongoDB (for running without internet)
// Use: mongodb://localhost:27017/quizdb
// ---------------------------------------------------------
const MONGO_URI = 'mongodb://localhost:27017/quizdb';
// const MONGO_URI = 'mongodb+srv://YOUR_USER:YOUR_PASS@cluster.mongodb.net/quizdb';

const connectDB = async () => {
  try {
    await mongoose.connect(MONGO_URI, {
      useNewUrlParser: true,
      useUnifiedTopology: true,
    });
    console.log('✅ MongoDB connected successfully');
    console.log('   [PaaS] Database running on cloud/local MongoDB');
  } catch (err) {
    console.error('❌ MongoDB connection error:', err.message);
    console.log('   Tip: Start local MongoDB or use Atlas URI');
    process.exit(1);
  }
};

// -------------------------------------------------------
// SCHEMAS
// -------------------------------------------------------

// User Schema — stores registered users
const userSchema = new mongoose.Schema({
  username: { type: String, required: true, unique: true },
  password: { type: String, required: true },  // stored as bcrypt hash
  email:    { type: String, required: true },
  createdAt: { type: Date, default: Date.now },
});

// Quiz Question Schema — stores MCQ questions
const questionSchema = new mongoose.Schema({
  question: { type: String, required: true },
  options:  { type: [String], required: true },  // array of 4 options
  answer:   { type: Number, required: true },     // index of correct option (0-3)
  category: { type: String, default: 'General' },
});

// Score Schema — stores each quiz attempt result
// [HDFS] These records are also written to /logs/sample.log
// and can be pushed to HDFS for big data analysis
const scoreSchema = new mongoose.Schema({
  username:  { type: String, required: true },
  score:     { type: Number, required: true },
  total:     { type: Number, required: true },
  timeTaken: { type: Number, default: 0 },       // seconds
  date:      { type: Date, default: Date.now },
});

const User     = mongoose.model('User',     userSchema);
const Question = mongoose.model('Question', questionSchema);
const Score    = mongoose.model('Score',    scoreSchema);

module.exports = { connectDB, User, Question, Score };
