package com.donfyy.crowds

import java.io.BufferedInputStream
import java.io.FileInputStream
import java.io.InputStream
import java.net.URL
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory


fun ca() {
    // Load CAs from an InputStream
    // (could be from a resource or ByteArrayInputStream or ...)
    val cf: CertificateFactory = CertificateFactory.getInstance("X.509")
    // From https://www.washington.edu/itconnect/security/ca/load-der.crt
    val caInput: InputStream = BufferedInputStream(FileInputStream("load-der.crt"))
    val ca: X509Certificate = caInput.use {
        cf.generateCertificate(it) as X509Certificate
    }
    System.out.println("ca=" + ca.subjectDN)

    // Create a KeyStore containing our trusted CAs
    val keyStoreType = KeyStore.getDefaultType()
    val keyStore = KeyStore.getInstance(keyStoreType).apply {
        load(null, null)
        setCertificateEntry("ca", ca)
    }

    // Create a TrustManager that trusts the CAs inputStream our KeyStore
    val tmfAlgorithm: String = TrustManagerFactory.getDefaultAlgorithm()
    val tmf: TrustManagerFactory = TrustManagerFactory.getInstance(tmfAlgorithm).apply {
        init(keyStore)
    }

    // Create an SSLContext that uses our TrustManager
    val context: SSLContext = SSLContext.getInstance("TLS").apply {
        init(null, tmf.trustManagers, null)
    }

    // Tell the URLConnection to use a SocketFactory from our SSLContext
    val url = URL("https://certs.cac.washington.edu/CAtest/")
    val urlConnection = url.openConnection() as HttpsURLConnection
    urlConnection.sslSocketFactory = context.socketFactory
    val inputStream: InputStream = urlConnection.inputStream
//    copyInputStreamToOutputStream(inputStream, System.out)

}