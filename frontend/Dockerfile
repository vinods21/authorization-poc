FROM node:22-alpine
WORKDIR /app
COPY package.json ./
COPY index.html ./
COPY src ./src
COPY vite.config.js ./
RUN npm install
EXPOSE 3000
CMD ["npm", "run", "dev", "--", "--host", "0.0.0.0"]
