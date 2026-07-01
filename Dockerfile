# Runs the Selenium/TestNG suite headlessly, with Chrome baked in.
FROM maven:3.9-eclipse-temurin-17

# Install Chrome (headless) + its runtime deps
RUN apt-get update && apt-get install -y \
    wget \
    gnupg \
    unzip \
    --no-install-recommends \
    && wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | gpg --dearmor -o /usr/share/keyrings/google-chrome.gpg \
    && echo "deb [arch=amd64 signed-by=/usr/share/keyrings/google-chrome.gpg] http://dl.google.com/linux/chrome/deb/ stable main" > /etc/apt/sources.list.d/google-chrome.list \
    && apt-get update && apt-get install -y google-chrome-stable --no-install-recommends \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY pom.xml .
# Warm the dependency cache as its own layer so code edits don't refetch everything
RUN mvn -B dependency:go-offline

COPY src ./src

# headless=true forces --headless=new in BaseTest; WebDriverManager auto-resolves
# the matching chromedriver for whatever Chrome version got installed above.
CMD ["mvn", "-B", "test", "-Dheadless=true"]
