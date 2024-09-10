module.exports = {
    devServer: {
        port: 8080, // Port dla frontend
        proxy: 'http://localhost:8081' // Port dla backendu
    }
};
