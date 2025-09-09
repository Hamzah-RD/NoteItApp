##📒 NoteIt – Smart Notes App

NoteIt is a modern Android note-taking application built with Java and Firebase Firestore. It helps users create, edit, search, and organize notes with colorful backgrounds and timestamps.

🚀 Features
✍️ Create Notes – Add title, subtitle, and detailed note text.
🎨 Custom Colors – Choose background colors for notes.
🕒 Auto Date & Time – Notes are timestamped automatically.
🔍 Search & Filter – Quickly find notes by title or subtitle.
📝 Update Notes – Edit and save existing notes.
☁️ Cloud Storage – Notes are securely stored using Firebase Firestore.
🎯 Responsive UI – Built with RecyclerView and modern Material Design.
🛠️ Tech Stack
Language: Java

UI: XML, RecyclerView, CardView, BottomSheet

Backend: Firebase Authentication & Firestore Database

Libraries:

Google Material Components
RoundedImageView
📦 Installation
Clone the repository:

git clone https://github.com/YourUsername/NoteItApp.git
Open the project in Android Studio.

Connect your app to Firebase (use your own google-services.json).

Build & run on emulator or device.

📌 Project Structure
app/
 ├── java/com/example/noteit/
 │    ├── CreateNewNote.java       # Create note activity
 │    ├── NotesAdapter.java        # RecyclerView adapter
 │    ├── NotesModel.java          # Data model
 │    ├── UpdateActivity.java      # Update existing notes
 │    └── MainActivity.java        # Dashboard of notes
 ├── res/layout/                   # All XML UI layouts
 └── res/drawable/                 # Icons & backgrounds
👨‍💻 Author
Hamza Rasheed 📌 Software Developer | Mobile App Enthusiast
