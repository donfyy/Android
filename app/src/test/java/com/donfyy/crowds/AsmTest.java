package com.donfyy.crowds;

import android.webkit.CookieManager;

import org.junit.Test;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.TypePath;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class AsmTest {

    @Test
    public void test1() throws IOException {
        System.out.println(new File(".").getCanonicalPath());
//        new ClassReader()
//        /Users/bytedance/Project/donfyy/Android/app/build/intermediates/javac/debug/classes/com/donfyy/crowds/asm
        ClassReader classReader = new ClassReader(new FileInputStream(new File("build/intermediates/javac/debug/classes/com/donfyy/crowds/asm/XxxPlugin$XxxRequest.class")));
        ClassVisitor visitor = new ClassVisitor(Opcodes.ASM5) {
            @Override
            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                System.out.println("version: " + version + " access:" + access + " name:" + name + " signature:" + signature + " superName:" + superName + " interfaces:" + Arrays.toString(interfaces));
                super.visit(version, access, name, signature, superName, interfaces);
            }

            @Override
            public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
                System.out.println("access:" + access + " name:" + name + " descriptor:" + descriptor + " signature:" + signature + " value:" + value);
                FieldVisitor innerVisitor = super.visitField(access, name, descriptor, signature, value);
                FieldVisitor fieldVisitor = new FieldVisitor(api, innerVisitor) {

                    @Override
                    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                        System.out.println("visitAnnotation descriptor:" + descriptor + " visible:" + visible);
                        AnnotationVisitor innerVisitor = super.visitAnnotation(descriptor, visible);
                        return new AnnotationVisitor(api, innerVisitor) {
                            @Override
                            public void visit(String name, Object value) {
                                System.out.println("AnnotationVisitor.visit name:" + name + " value:" + value);
                                super.visit(name, value);
                            }

                            @Override
                            public void visitEnum(String name, String descriptor, String value) {
                                System.out.println("AnnotationVisitor.visitEnum name:" + name + " descriptor:" + descriptor + " value:" + value);
                                super.visitEnum(name, descriptor, value);
                            }

                            @Override
                            public AnnotationVisitor visitAnnotation(String name, String descriptor) {
                                System.out.println("AnnotationVisitor.visitAnnotation name:" + name + " descriptor:" + descriptor);
                                return super.visitAnnotation(name, descriptor);
                            }

                            @Override
                            public AnnotationVisitor visitArray(String name) {
                                System.out.println("AnnotationVisitor.visitArray name:" + name );

                                return super.visitArray(name);
                            }

                            @Override
                            public void visitEnd() {
                                System.out.println("AnnotationVisitor.visitEnd "  );
                                super.visitEnd();
                            }
                        };
                    }

                    @Override
                    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
                        System.out.println("visitTypeAnnotation descriptor:" + descriptor + " visible:" + visible + " typePath:" + typePath + " typeRef:" + typeRef);
                        return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
                    }

                    @Override
                    public void visitAttribute(Attribute attribute) {
                        System.out.println("visitAttribute :" + attribute);
                        super.visitAttribute(attribute);
                    }

                    @Override
                    public void visitEnd() {
                        System.out.println("visit end");
                        super.visitEnd();
                    }
                };
                return fieldVisitor;
            }

            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                return super.visitMethod(access, name, descriptor, signature, exceptions);
            }

            @Override
            public void visitEnd() {
                super.visitEnd();
            }
        };
        classReader.accept(visitor, 0);
    }


}