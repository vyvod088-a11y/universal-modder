# Beaverworks — журнал

Запрос: Minecraft 1.20.1, Forge, бобровая броня и инструменты.
Маршрут: отдельный Forge-мод с DeferredRegister; без изменений toolkit и игровых установок.
Подготовлены 10 предметов, рецепты, локализации, текстуры, эффект полного комплекта и бонус топора.
Сборка: Forge 47.4.0, ForgeGradle 6, Gradle 8.8, JDK 17.
Ограничения: сервер maven.minecraftforge.net отвечает CONNECT 403, домены Forge/Mojang сохранены в черновик среды, но политика пока не применена. В среде есть JDK 21, JDK 17 ещё не установлен. Сборка и игра не проверены.
Следующий шаг: применить разрешения сети в настройках среды, установить JDK 17, выполнить ./gradlew build, затем runClient и проверки README. Не принимать исходный проект за проверенный релиз.

Проверка: tools/check_resources.py проходит. Gradle 8.8 загружен через системный HTTPS-прокси и сверил SHA-256. Gradle build остановлен на разрешении ForgeGradle; повторный curl к Maven Forge подтвердил CONNECT 403. Без прокси Java выдаёт UnknownHostException. Для облачной сборки передавайте https.proxyHost/https.proxyPort и http.proxyHost/http.proxyPort через -D, значения берите из системного прокси, не меняя политику сети.
