# 📜 Histora

🧠 **Histora** is a clean, AI-powered quiz app that challenges your history knowledge with true-or-false questions generated on the fly. Built using **Jetpack Compose** and Google's **Gemini AI**, it delivers a fun and informative learning experience in a beautifully designed UI.

---

## ✨ Features

- 🧠 Auto-generates historical questions with increasing difficulty  
- ✅ True-or-false answer format  
- 📊 Progress bar and question count tracking  
- 📜 Detailed explanations for each question  
- 🔁 Quiz restart option

---

## 🚀 Getting Started

### 📦 Clone the Repo

```bash
git clone https://github.com/S-P-H-E/Histora.git
cd Histora
```

### 🧑‍💻 Open in Android Studio

1. Launch **Android Studio**  
2. Open the cloned folder  
3. Connect an emulator or physical device  
4. Press **Run** ▶️

> 📌 Make sure to add your Gemini API key in `local.properties`:

```properties
HISTORA_API_KEY=your_api_key_here
```

---

## 🧠 How the AI Works

Histora uses **Gemini 2.0 Flash** via the [Google Generative AI SDK](https://ai.google.dev/).

Here's the core prompt used:

```kotlin
val prompt = """
Give me 5 unique historical true-or-false questions with increasing difficulty.
Each must include a \"question\" (string), an \"answer\" (boolean), and an \"explanation\" (string).
Return strictly as a JSON array. No extra text. No \"True or False\" text before the question.
"""
```

> Responses are parsed and shown in-app with explanations for learning.

---

## 📸 Screenshots

---

## 💪 Tech Stack

- **Kotlin**  
- **Jetpack Compose**
- **Google Generative AI SDK**  
- **Gemini 2.0 Flash Model**

---

## 📄 License

MIT License © 2025 [Siphesihle Mbuyisa](https://github.com/S-P-H-E)

---
