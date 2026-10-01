# Google Play Console Submission & Policy Declarations Guide

यह गाइड आपको Google Play Console में **"App content" (ऐप सामग्री)** और सभी आवश्यक पॉलिसी फॉर्म्स को 100% सही तरीके से भरने में मदद करेगी।

---

### App Identity Details
- **App Name (शीर्षक):** Task & Notepad
- **Short Description (छोटा विवरण - Max 80 chars):** Manage daily to-do tasks, checklists, and notes with offline privacy.
- **Full Description (विस्तृत विवरण):**
```
Task & Notepad is a clean, simple, and private productivity app designed to keep your daily life organized. Seamlessly track your daily tasks, to-do lists, and personal notes all in one place.

Key Features:
• Dual Experience: Effortlessly switch between Tasks and Notes with a single tap.
• Smart Task Management: Set priorities (High, Medium, Low), add categories, set due dates, and track your completion progress.
• Color-Coded Notepad: Write quick ideas, pin important notes, and customize note card colors for easy scanning.
• Instant Search: Quickly find tasks and notes by title, tag, or description.
• 100% Offline & Private: No accounts, no cloud tracking. All your data stays safely stored on your device only.
• Clean Material 3 Design: Beautiful, accessible, and fast user interface.
```
- **Package Name:** `com.aistudio.tasknotepad.kxvtnp`
- **Category:** Productivity (उत्पादकता) / Tools
- **Contact Email:** `info.chittaranjanbag@gmail.com`

---

### 1. Privacy Policy (प्राइवेसी पॉलिसी लिंक)
Google Play Console > **Policy and programs** > **App content** > **Privacy Policy**:
- **Privacy Policy URL:** आप अपने रिपॉजिटरी में मौजूद `/docs/privacy_policy.html` को किसी भी फ्री सर्विस (Google Sites, GitHub Pages, Firebase Hosting, Notion, या Blogger) पर डालकर वह URL यहाँ पेस्ट करें।
- **Policy File in Project:** `/docs/privacy_policy.html` और `/docs/PRIVACY_POLICY.md`

---

### 2. Data Safety (डेटा सुरक्षा घोषणा)
Google Play Console > **App content** > **Data safety**:

1. **Does your app collect or share any of the required user data types?**
   - उत्तर: **No (नहीं)**
   *(क्योंकि सारा डेटा सिर्फ यूजर के फोन पर SQLite/Room डेटाबेस में ऑफलाइन रहता है)*

2. **Is all of the user data collected by your app encrypted in transit?**
   - उत्तर: **Not applicable / Yes** (कोई डेटा नेटवर्क पर भेजा ही नहीं जाता)

3. **Do you provide a way for users to request that their data be deleted?**
   - उत्तर: **Yes** (उपयोगकर्ता सीधे ऐप से टास्क/नोट डिलीट कर सकते हैं या ऐप अनइंस्टॉल करके सारा डेटा हटा सकते हैं)

---

### 3. Ads (विज्ञापन घोषणा)
Google Play Console > **App content** > **Ads**:
- **Does your app contain ads?**
  - उत्तर: **No, my app does not contain ads (नहीं, मेरे ऐप में विज्ञापन नहीं हैं)**

---

### 4. App Access (ऐप एक्सेस / क्रेडेंशियल)
Google Play Console > **App content** > **App access**:
- **All functionality is available without special access** (सभी सुविधाएं बिना लॉगिन या पासवर्ड के उपलब्ध हैं)
  - उत्तर: **All functionality is available without restrictions**

---

### 5. Target Audience and Content (लक्षित दर्शक)
Google Play Console > **App content** > **Target audience and content**:
- **Target Age Groups:**
  - Select: **13-15, 16-17, 18 and over** (या All ages)
- **Could your store listing appeal to children?**
  - उत्तर: **No** (यह एक सामान्य यूटिलिटी/प्रोडक्टिविटी टूल है)

---

### 6. Content Rating (IARC सामग्री रेटिंग प्रश्नावली)
Google Play Console > **App content** > **Content ratings**:
- **Category:** Productivity, Utility, Tools, or Other
- **Violence (हिंसा):** No
- **Sexuality (यौन सामग्री):** No
- **Language / Profanity (अभद्र भाषा):** No
- **Controlled Substances (अवैध पदार्थ/शराब/तंबाकू):** No
- **User Interaction / Location Sharing:** No
- **Result:** आपको **PEGI 3 / ESRB Everyone / 3+** रेटिंग प्राप्त होगी।

---

### 7. Government Apps
- **Is your app developed by or on behalf of a government?**
  - उत्तर: **No**

---

### 8. Financial Features (वित्तीय सुविधाएं)
- **Does your app provide financial features?**
  - उत्तर: **My app doesn't provide any financial features**

---

### 9. News App
- **Is your app a news app?**
  - उत्तर: **No**

---

### 10. COVID-19 Contact Tracing and Status Apps
- **Is your app a COVID-19 contact tracing app?**
  - उत्तर: **My app is not a publicly available COVID-19 contact tracing or status app**
