package com.ontochem.chemistry;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.actelion.research.chem.RingCollection;
import com.actelion.research.chem.StereoMolecule;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

/**
 * Rule-based filter for screening compounds with abnormal valencies and ring counts.
 * 
 * <h3>Changelog</h3>
 * <ul>
 *   <li>2023-06-23
 *     <ul>
 *       <li>Added method to flag abnormal valencies</li>
 *     </ul>
 *   </li>
 * </ul>
 * 
 * @author Shadrack J. Barnabas
 * @date 2023-09-19
 */
public class RuleBasedFilter {
    static Logger LOG = Logger.getLogger(RuleBasedFilter.class.getName());

    /**
     * Check if a molecule has abnormal valencies using the specified module.
     * 
     * @param smiles  The input SMILES string of the molecule.
     * @param module  The module to use for checking (e.g., "ocl" or "cdk").
     * @return True if the molecule has abnormal valencies, false otherwise.
     */
    public static boolean isMoleculeAbnormallyValent(String smiles, String module) {
        boolean isAbnormal = false;
        try {
            if (module.equals("ocl")) {
                isAbnormal = isAbnormallyValentOcl(smiles);
            } else if (module.equals("cdk") || module.equals("ambit")) {
                isAbnormal = isAbnormallyValentCdk(smiles);
            } else {
                LOG.info("Module not found.");
            }
        } catch (Exception e) {
            LOG.info("SMILES " + smiles + " cannot be parsed using module " + module + ". Error: " + e);
        }
        return isAbnormal;
    }

    /**
     * Check if the ring count of a molecule is abnormal using the specified module.
     * 
     * @param smiles    The input SMILES string of the molecule.
     * @param module    The module to use for checking (e.g., "ocl" or "cdk").
     * @param threshold The threshold for the ring count.
     * @return True if the ring count is abnormal, false otherwise.
     */
    public static boolean isRingCountAbnormal(String smiles, String module, int threshold) {
        boolean isAbnormal = false;
        try {
            if (module.equals("ocl")) {
                isAbnormal = isRingCountAbnormalOcl(smiles, threshold);
            } else if (module.equals("cdk") || module.equals("ambit")) {
                LOG.info("Module not found.");
            } else {
                LOG.info("Module not found.");
            }
        } catch (Exception e) {
            LOG.info("SMILES " + smiles + " cannot be parsed using module " + module + ". Error: " + e);
        }
        return isAbnormal;
    }

    /**
     * Convert a SMILES string to a StereoMolecule.
     * 
     * @param smiles The input SMILES string.
     * @return A StereoMolecule representation of the molecule.
     */
    public static StereoMolecule ConvertSmilesToStereoMolecule(String smiles) {
        try {
            int mode = com.actelion.research.chem.SmilesParser.SMARTS_MODE_IS_SMILES;

            StereoMolecule mol = new StereoMolecule();
            com.actelion.research.chem.SmilesParser sp = new com.actelion.research.chem.SmilesParser(mode, false);
            sp.parse(mol, smiles);
            com.actelion.research.chem.MoleculeStandardizer.standardize(mol, 0);
            return mol;
        } catch (Exception e) {
            LOG.info("SMILES cannot be parsed using OpenChemLib: " + smiles + ". Error: " + e);
        }
        return null;
    }

    /**
     * Check if a molecule has abnormal valencies using OpenChemLib.
     * 
     * @param smiles The input SMILES string of the molecule.
     * @return True if the molecule has abnormal valencies, false otherwise.
     */
    public static boolean isAbnormallyValentOcl(String smiles) {
        boolean isAbnormal = false;
        try {
            StereoMolecule mol = ConvertSmilesToStereoMolecule(smiles);
            if (mol != null) {
                for (int i = 0; i < mol.getAllAtoms(); i++) {
                    int val = mol.getAtomAbnormalValence(i);
                    if (val != -1) {
                        isAbnormal = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            isAbnormal = true;
            LOG.info("Error calculating abnormal valencies using OpenChemLib: " + e);
        }
        return isAbnormal;
    }

    /**
     * Check if a molecule has abnormal valencies using CDK (Chemistry Development Kit).
     * 
     * @param smiles The input SMILES string of the molecule.
     * @return True if the molecule has abnormal valencies, false otherwise.
     */
    public static boolean isAbnormallyValentCdk(String smiles) {
        boolean isAbnormallyValent = false;
        try {
            SmilesParser parser = new SmilesParser(DefaultChemObjectBuilder.getInstance());
            IAtomContainer molecule = parser.parseSmiles(smiles);
            AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);

            for (int i = 0; i < molecule.getAtomCount(); i++) {
                int valence = molecule.getAtom(i).getValency();
                int expectedValence = getExpectedValency(molecule.getAtom(i).getAtomTypeName());
                if (expectedValence != -1) {
                    if (valence > expectedValence) {
                        isAbnormallyValent = true;
                        break;
                    }
                } else {
                    LOG.info("Unknown element present in SMILES: " + smiles + ". Check expected valency or add elements to the expected valency library in class RuleBasedFilter.");
                    break;
                }
            }
        } catch (CDKException e) {
            isAbnormallyValent = true;
            LOG.info("Error calculating abnormal valencies using CDK: " + e);
        }
        return isAbnormallyValent;
    }

    /**
     * Get the expected valency of an element.
     * 
     * @param element The element symbol (e.g., "C" for carbon).
     * @return The expected valency of the element.
     */
    public static int getExpectedValency(String element) {
        int expValency = -1;
        try {
            Map<String, Integer> EXPECTED_VALENCIES = new HashMap<String, Integer>();
            // Add expected valencies for elements here
            EXPECTED_VALENCIES.put("H", 1);
            // ... Add more elements and their valencies ...
            expValency = EXPECTED_VALENCIES.get(element);
        } catch (Exception e) {
            LOG.info("Expected valencies not found for element " + element + ": " + e);
        }
        return expValency;
    }

    /**
     * Check if the ring count of a molecule is abnormal using OpenChemLib.
     * 
     * @param smiles    The input SMILES string of the molecule.
     * @param threshold The threshold for the ring count.
     * @return True if the ring count is abnormal, false otherwise.
     */
    public static boolean isRingCountAbnormalOcl(String smiles, int threshold) {
        boolean isAbnormal = true;
        try {
            StereoMolecule mol = ConvertSmilesToStereoMolecule(smiles);
            if (mol != null) {
                int val = countRings(mol);
                if (val < threshold) {
                    isAbnormal = false;
                }
            }
        } catch (Exception e) {
            LOG.info("Error calculating ring counts using OpenChemLib: " + e);
        }
        return isAbnormal;
    }

    /**
     * Count the rings in a StereoMolecule.
     * 
     * @param molecule The input StereoMolecule.
     * @return The count of rings.
     */
    public static int countRings(StereoMolecule molecule) {
        int mode = RingCollection.MODE_SMALL_AND_LARGE_RINGS_AND_AROMATICITY;
        RingCollection ringCollection = new RingCollection(molecule, mode);
        return ringCollection.getSize();
    }
}
