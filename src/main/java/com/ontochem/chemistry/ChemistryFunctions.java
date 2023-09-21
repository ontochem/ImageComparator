package com.ontochem.chemistry;

import java.util.ArrayList;

import org.openmolecules.chem.conf.gen.ConformerGenerator;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IReaction;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmiFlavor;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import com.actelion.research.chem.Molecule;
import com.actelion.research.chem.MoleculeStandardizer;
import com.actelion.research.chem.StereoMolecule;
import com.ontochem.imageHandler.ImageComparator;

/**
 * <h3>Changelog</h3>
 * <ul>
 *   <li>2023-06-14
 *     <ul>
 *       <li>Added chemistry-related functions such as adding explicit hydrogens to the structure,
 *           kekulizing/aromatizing the structures using Chemistry Development Kit</li>
 *     </ul>
 *   </li>
 * </ul>
 * 
 * @author Shadrack J. Barnabas
 * @email shadrack.j.barnabas@ontochem.com
 * @date 2023-09-19
 */
public class ChemistryFunctions {
    
    ImageComparator imageComparator;
    public static IChemObjectBuilder builder = SilentChemObjectBuilder.getInstance();

    public ChemistryFunctions(ImageComparator imageComparator) {
        this.imageComparator = imageComparator;
    }
     
    /**
     * Adds explicit hydrogens to a SMILES structure.
     * 
     * @param smiles The input SMILES structure.
     * @return SMILES structure with explicit hydrogen atoms.
     */
    public static String addExplicitH(String smiles) {
        try {
            SmilesGenerator smiGen = new SmilesGenerator(SmiFlavor.Isomeric);
            IAtomContainer mol = processSmi(smiles);

            // Add explicit hydrogen atoms
            CDKHydrogenAdder.getInstance(mol.getBuilder());
            AtomContainerManipulator.convertImplicitToExplicitHydrogens(mol);

            return smiGen.create(mol);
        } catch (Exception e) {
            // Handle exceptions
            e.printStackTrace();
        }
        return smiles;
    }

    /**
     * Adds explicit hydrogens to a target structure, optionally for a reaction.
     * 
     * @param target     The input structure in SMILES format.
     * @param isAReaction Whether the target represents a reaction.
     * @return SMILES structure with explicit hydrogen atoms.
     */
    public static String addExplicitH(String target, boolean isAReaction) {
        String targetNew = "";
        try {
            if (isAReaction) {
                targetNew = addExplicitHRsmi(target);
            } else {
                targetNew = addExplicitH(target);
            }
        } catch (Exception e) {
            // Handle exceptions
            e.printStackTrace();
        }
        return targetNew;
    }

    /**
     * Adds explicit hydrogens to a reaction SMILES structure.
     * 
     * @param rsmiles The input reaction SMILES structure.
     * @return Reaction SMILES structure with explicit hydrogen atoms.
     */
    public static String addExplicitHRsmi(String rsmiles) {
        String fReaction = "";
        String rsmi = rsmiles.replaceAll("\\.\\.", ".");
        try {
            SmilesGenerator smiGen = new SmilesGenerator(SmiFlavor.UseAromaticSymbols);
            IReaction rxnTarget = processRsmi(rsmi);
            IAtomContainerSet reactantContainerSet = rxnTarget.getReactants();
            ArrayList<String> reactantList = new ArrayList<>();
            // Process reactants
            for (int i = 0; i < rxnTarget.getReactants().getAtomContainerCount(); i++) {
                String react = (smiGen.create(reactantContainerSet.getAtomContainer(i)));
                reactantList.add(addExplicitH(react));
            }
            // Process agents
            ArrayList<String> agentList = new ArrayList<>();
            IAtomContainerSet agentContainerSet = rxnTarget.getAgents();
            for (int i = 0; i < rxnTarget.getAgents().getAtomContainerCount(); i++) {
                String react = (smiGen.create(agentContainerSet.getAtomContainer(i)));
                agentList.add(addExplicitH(react));
            }
            // Process products
            ArrayList<String> productList = new ArrayList<>();
            IAtomContainerSet productContainerSet = rxnTarget.getProducts();
            for (int i = 0; i < rxnTarget.getProducts().getAtomContainerCount(); i++) {
                String react = (smiGen.create(productContainerSet.getAtomContainer(i)));
                productList.add(addExplicitH(react));
            }
            String fReactant = removeTrailingCharacter(removeLeadingCharacter(reactantList.toString().replaceAll(", ", "..")));
            String fAgent = removeTrailingCharacter(removeLeadingCharacter(agentList.toString().replaceAll(", ", "..")));
            String fProduct = removeTrailingCharacter(removeLeadingCharacter(productList.toString().replaceAll(", ", "..")));
            fReaction = fReactant + ">" + fAgent + ">" + fProduct;
        } catch (Exception e) {
            // Handle exceptions
            e.printStackTrace();
        }
        return fReaction;
    }

    /**
     * Removes the leading character from a string.
     * 
     * @param s The input string.
     * @return The string with the leading character removed.
     */
    public static String removeLeadingCharacter(String s) {
        String result = s.substring(1);
        return result;
    }

    /**
     * Removes the trailing character from a string.
     * 
     * @param s The input string.
     * @return The string with the trailing character removed.
     */
    public static String removeTrailingCharacter(String s) {
        return (s == null || s.length() == 0)
          ? null 
          : (s.substring(0, s.length() - 1));
    }

    /**
     * Gets a SMILES parser of Chemistry Development Kit.
     * 
     * @return A SMILES parser.
     * @throws Exception
     */
    public static SmilesParser getSmilesParser() throws Exception {
        return new org.openscience.cdk.smiles.SmilesParser(builder);
    }

    /**
     * Parses a SMILES structure and returns an IAtomContainer.
     * 
     * @param smi The input SMILES structure.
     * @return IAtomContainer of Chemistry Development Kit.
     * @throws Exception
     */
    public static IAtomContainer processSmi(String smi) throws Exception {
        return new org.openscience.cdk.smiles.SmilesParser(builder).parseSmiles(smi);
    }

    /**
     * Parses a reaction SMILES structure and returns an IReaction.
     * 
     * @param smi The input reaction SMILES structure.
     * @return IReaction of Chemistry Development Kit.
     * @throws Exception
     */
    public static IReaction processRsmi(String smi) throws Exception {
        return new org.openscience.cdk.smiles.SmilesParser(builder).parseReactionSmiles(smi);
    }

    /**
     * Aromatizes a structure.
     * 
     * @param rsmiles     The input reaction SMILES structure or SMILES structure.
     * @param isAReaction Whether the input represents a reaction.
     * @return Aromatized structure.
     */
    public static String aromatizeStructure(String rsmiles, boolean isAReaction) {
        String nSmiles = rsmiles.replaceAll("\\.\\.", ".");
        SmilesGenerator smiGen = new SmilesGenerator(SmiFlavor.UseAromaticSymbols);
        try {
            if (isAReaction) {
                IReaction rxnTarget = processRsmi(nSmiles);	    
                nSmiles = smiGen.createReactionSMILES(rxnTarget);
            } else {
                IAtomContainer molTarget = processSmi(nSmiles);
                nSmiles = smiGen.createSMILES(molTarget);
            }
            return nSmiles;
        } catch (Exception e) {
            // Handle exceptions
            e.printStackTrace();
        }
        return nSmiles;
    }

    /**
     * Kekulizes a structure.
     * 
     * @param structure   The input structure in SMILES format or reaction SMILES format.
     * @param isAReaction Whether the input represents a reaction.
     * @return Kekulized structure.
     */
    public static String kekulizeStructure(String structure, boolean isAReaction) {
        String nSmiles = structure.replaceAll("\\.\\.", ".");
        SmilesGenerator smiGen = new SmilesGenerator(SmiFlavor.Isomeric);
        try {
            if (isAReaction) {
                SmilesParser sp = getSmilesParser();
                sp.kekulise(true);
                IReaction reaction = sp.parseReactionSmiles(nSmiles);
                nSmiles = smiGen.createReactionSMILES(reaction);
            } else {
                SmilesParser sp = getSmilesParser();
                sp.kekulise(true);
                IAtomContainer molecule = sp.parseSmiles(nSmiles);
                nSmiles = smiGen.createSMILES(molecule);
            }
            return nSmiles;
        } catch (Exception e) {
            // Handle exceptions
            e.printStackTrace();
        }
        return nSmiles;
    }

    /**
     * Sets the aromaticity of a StereoMolecule.
     * 
     * @param mMolecule The input StereoMolecule.
     */
    public static void setAromatic(StereoMolecule mMolecule) {
        mMolecule.ensureHelperArrays(Molecule.cHelperRings);
        for (int bond = 0; bond < mMolecule.getBonds(); bond++) {
            if (mMolecule.isDelocalizedBond(bond)) {
                mMolecule.changeBond(bond, Molecule.cBondTypeDelocalized);
            }
        }
    }

    /**
     * Processes a StereoMolecule based on a specified style (add explicit hydrogens,
     * aromatize, or kekulize).
     * 
     * @param mol    The input StereoMolecule.
     * @param style  The style to apply (addExplicitHydrogen, aromatize, or kekulize).
     * @return The processed StereoMolecule.
     * @throws Exception
     */
    public static StereoMolecule processSmi(StereoMolecule mol, String style) throws Exception {
        // Molecule standardizer kekulizes the structures
        MoleculeStandardizer.standardize(mol, 0);
        if (!style.equals("kekulize")) {
            if (style.equals("aromatize")) {
                setAromatic(mol);
            }
            if (style.equals("addExplicitHydrogen")) {
                ConformerGenerator.addHydrogenAtoms(mol);
            }
        }
        return mol;
    }
}
