package de.unisb.cs.st.javalanche.mutation.bytecodeMutations;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.util.CheckMethodAdapter;
import de.unisb.cs.st.javalanche.mutation.bytecodeMutations.arithmetic.PossibilitiesArithmeticReplaceMethodAdapter;
import de.unisb.cs.st.javalanche.mutation.bytecodeMutations.negateJumps.NegateJumpsPossibilitiesMethodAdapter;
import de.unisb.cs.st.javalanche.mutation.bytecodeMutations.removeCalls.MyAdviceAdapter;
import de.unisb.cs.st.javalanche.mutation.bytecodeMutations.removeCalls.RemoveCallsPossibilitiesMethodAdapter;
import de.unisb.cs.st.javalanche.mutation.bytecodeMutations.replaceIntegerConstant.PossibilitiesRicMethodAdapter;
import de.unisb.cs.st.javalanche.mutation.mutationPossibilities.MutationPossibilityCollector;

public class MutationsCollectorClassAdapter extends ClassAdapter {

	private String className;

	private boolean debug = true;

	private final MutationPossibilityCollector mpc;

	private Map<Integer, Integer> ricPossibilities = new HashMap<Integer, Integer>();

	private Map<Integer, Integer> arithmeticPossibilities = new HashMap<Integer, Integer>();

	private Map<Integer, Integer> negatePossibilities = new HashMap<Integer, Integer>();

	private Map<Integer, Integer> removeCallsPossibilities = new HashMap<Integer, Integer>();

	public MutationsCollectorClassAdapter(ClassVisitor cv,
			MutationPossibilityCollector mpc) {
		super(cv);
		this.mpc = mpc;
	}

	@Override
	public void visit(int version, int access, String name, String signature,
			String superName, String[] interfaces) {
		super.visit(version, access, name, signature, superName, interfaces);
		className = name;
	}

	public MethodVisitor visitMethod(int access, String name, String desc,
			String signature, final String[] exceptions) {
		MethodVisitor mv = super.visitMethod(access, name, desc, signature,
				exceptions);
		if (debug) {
			mv = new CheckMethodAdapter(mv);
		}

		mv = new PossibilitiesRicMethodAdapter(mv, className, name, mpc,
				ricPossibilities);
		mv = new NegateJumpsPossibilitiesMethodAdapter(mv, className, name,
				mpc, negatePossibilities);
		mv = new PossibilitiesArithmeticReplaceMethodAdapter(mv, className,
				name, mpc, arithmeticPossibilities);
//		mv = new RemoveCallsPossibilitiesMethodAdapter(new MyAdviceAdapter(mv,access,name,desc), className, name,
//				mpc, removeCallsPossibilities);
		return mv;
	}
}
