<div align="center" dir="rtl">
  <img src="docs/icon.png" alt="أيقونة MGN AI" width="100" />
  <h1>MGN AI</h1>

  <p>
    تطبيق أندرويد أصلي للمحادثة مع نماذج الذكاء الاصطناعي، بدعم كامل للغة العربية،
    وتبديل اللغة داخل التطبيق، وتحديثات تلقائية من إصداراته الخاصة، وخدمات مدعومة بـ Firebase.
  </p>

  <p>
    <img src="https://img.shields.io/badge/المنصة-Android-3DDC84?style=flat-square&logo=android&logoColor=white" alt="المنصة" />
    <img src="https://img.shields.io/badge/اللغة-Kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white" alt="اللغة" />
    <img src="https://img.shields.io/badge/الواجهة-Jetpack%20Compose-4285F4?style=flat-square&logo=jetpackcompose&logoColor=white" alt="الواجهة" />
    <img src="https://img.shields.io/badge/الترخيص-AGPL--3.0-blue?style=flat-square" alt="الترخيص" />
  </p>

  <p>
    العربية · <a href="README.md">English</a>
  </p>
</div>

---

## المحتويات

- [التحميل](#التحميل)
- [المميزات](#المميزات)
- [الإعداد والبناء](#الإعداد-والبناء)
- [البنية المعمارية](#البنية-المعمارية)
- [المساهمة](#المساهمة)
- [تاريخ النجوم](#تاريخ-النجوم)
- [الترخيص](#الترخيص)

<div dir="rtl">

## التحميل

**[التحميل من إصدارات GitHub ←](https://github.com/mgnegypt/eg-ai/releases)** (الطريقة الموصى بها)

ملفات APK المتوفرة في كل إصدار:

| ملف APK | الفئة المستهدفة |
|---|---|
| `mgn-ai-v<VERSION>-arm64-v8a-release.apk` | معظم الهواتف |
| `mgn-ai-v<VERSION>-x86_64-release.apk` | أجهزة/محاكيات x86_64 |
| `mgn-ai-v<VERSION>-universal-release.apk` | جميع المعماريات |

معرف الحزمة: `com.mgn.ai` (يُثبَّت جنبًا إلى جنب مع تطبيقات مشابهة دون تعارض).

## المميزات

**التعريب والمظهر**
- دعم عربي كامل مع اتجاه الكتابة من اليمين لليسار (RTL)، إضافة إلى الإنجليزية؛ مبدّل لغة فوري داخل التطبيق (النظام / الإنجليزية / العربية)
- هوية MGN البصرية — وضع فاتح أخضر/أبيض هادئ، ووضع داكن أخضر نيون — مع أنماط اختيارية إضافية

**الذكاء الاصطناعي والمزوّدون**
- دعم مزوّدين متعددين للذكاء الاصطناعي: API / رابط / نماذج مخصصة (متوافق مع OpenAI وGoogle وAnthropic)
- دعم إدخال متعدد الوسائط (صور، مستندات نصية، PDF، DOCX)
- إمكانية الوصول للويب للاستخدام متعدد المنصات
- دعم بروتوكول MCP
- تخصيص الوكلاء (Agents)
- خاصية ذاكرة تشبه ChatGPT
- ترجمة بالذكاء الاصطناعي

**تجربة المحادثة**
- عرض Markdown مع تلوين الأكواد، ومعادلات LaTeX، والجداول، ومخططات Mermaid
- تفرّع الرسائل (Message Branching)
- إمكانيات بحث متعددة (Exa، Tavily، Zhipu، LinkUp، Brave، Perplexity، وغيرها)
- متغيرات في الأوامر (اسم النموذج، الوقت، إلخ)
- استيراد بطاقات الشخصيات من SillyTavern

**أدوات وتكاملات**
- تصدير واستيراد إعدادات المزوّدين عبر رمز QR
- تخصيص ترويسات وأجسام طلبات HTTP
- فاحص تحديثات داخل التطبيق مرتبط بـ [إصدارات MGN AI](https://github.com/mgnegypt/eg-ai/releases)
- تبرعات عبر Binance، تُدار عن بُعد من خلال Firebase Remote Config
- التواصل مع المطوّر (فيسبوك، واتساب، تيليجرام) من الإعدادات ← حول التطبيق

## الإعداد والبناء

**المتطلبات:** JDK 17، Android SDK (بمستوى API 37 وأدوات البناء)، Node 22 + pnpm 11

```bash
git clone https://github.com/mgnegypt/eg-ai.git
cd eg-ai

# اختياري: ملف google-services.json حقيقي لـ com.mgn.ai (مشروع Firebase باسم mgn-ai)
# cp /path/to/google-services.json app/google-services.json

./gradlew assembleDebug     # نسخة تجريبية (package com.mgn.ai.debug)
./gradlew test              # اختبارات الوحدات على JVM
./gradlew lint              # فحص Android Lint
./gradlew assembleRelease   # نسخة إصدار موقّعة (تتطلب توقيعًا، انظر أدناه)
```

يشغّل نظام CI (`.github/workflows/ci.yml`) الأوامر `assembleDebug`، وفحص هوية APK واللغات (`aapt dump badging`)، والاختبارات، وLint مع كل push أو Pull Request.

## البنية المعمارية

| الوحدة | المسؤولية |
|---|---|
| `app` | الواجهة (Jetpack Compose، Navigation 3)، وViewModels، وفاحص التحديثات، ونظام اللغات |
| `ai` | طبقة تجريد مزوّدي الذكاء الاصطناعي (OpenAI، Google، Anthropic، …) |
| `common` / `document` / `highlight` / `material3` / `search` / `speech` / `videogen` / `workspace` / `oauth` / `web` | مكتبات الميزات |

تقع مساحات أسماء Kotlin الداخلية تحت `com.mgn.ai.*`، ومعرّف تطبيق أندرويد هو `com.mgn.ai`.

## المساهمة

يُطوَّر هذا المشروع باستخدام [Android Studio](https://developer.android.com/studio). قبل إرسال أي Pull Request، يُرجى قراءة [إرشادات المساهمة](CONTRIBUTING.md).

**التقنيات المستخدمة:**

| التقنية | الدور |
|---|---|
| [Kotlin](https://kotlinlang.org/) | لغة التطوير |
| [Koin](https://insert-koin.io/) | حقن التبعيات (Dependency Injection) |
| [Jetpack Compose](https://developer.android.com/jetpack/compose) | إطار عمل الواجهة |
| [DataStore](https://developer.android.com/topic/libraries/architecture/datastore) | تخزين بيانات التفضيلات |
| [Room](https://developer.android.com/training/data-storage/room) | قاعدة البيانات |
| [Coil](https://coil-kt.github.io/coil/) | تحميل الصور |
| [Material You](https://m3.material.io/) | تصميم الواجهة |
| [Navigation 3](https://developer.android.com/guide/navigation/navigation-3) | التنقل |
| [OkHttp](https://square.github.io/okhttp/) | عميل HTTP |
| [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) | تسلسل JSON |
| [Firebase](https://firebase.google.com/) | Crashlytics وAnalytics وRemote Config |

## 🌟 تاريخ النجوم

<div align="center">

[![Star History Chart](https://api.star-history.com/svg?repos=mgnegypt/eg-ai&type=Date)](https://star-history.com/#mgnegypt/eg-ai&Date)

</div>

## الترخيص

هذا المشروع مرخّص بموجب [رخصة جنو أفيرو العمومية العامة الإصدار 3.0](LICENSE) (AGPL-3.0).

حقوق النشر لـ MGN AI محفوظة لمطوّريه والمساهمين فيه فقط، ويُوزَّع بموجب نفس الرخصة. تم الحفاظ على إشعارات حقوق النشر الأصلية.

</div>
