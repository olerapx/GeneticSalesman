package genetic.algorithms;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Random;

import genetic.models.chromosome.Chromosome;
import genetic.models.function.ObjectiveFunction;
import genetic.models.population.Population;
import genetic.operators.CrossoverOperator;
import genetic.operators.MutationOperator;
import genetic.operators.ReductionOperator;
import genetic.operators.SelectionOperator;
import genetic.util.NumberGenerator;
import genetic.util.sig4j.signal.Signal1;

/**
 * Contains the basic genetic algorithms.
 */
public class GeneticAlgorithm 
{
	public final Signal1<String> sendLog = new Signal1<>();
	public final Signal1<Chromosome> sendChromosome = new Signal1<>();
	public final Signal1<Population> sendPopulation = new Signal1<>();
	
	private Population population;
	private ObjectiveFunction function;
	
	private int iterationCount;
	private Duration maxDuration;
	
	private Double crossoverChance;
	private Double inversionChance;
	private Double mutationChance;
	private Double transpositionChance;
	private Double duplicationAndDeletionChance;
	
	private SelectionOperator selection;
	private ReductionOperator reduction;
	
	private Random rand;
	
	public GeneticAlgorithm(Population population, ObjectiveFunction function, int iterationCount, String maxTime, 
			double crossoverChance, double inversionChance, double mutationChance, double transpositionChance, double duplicationAndDeletionChance)
	{		
		this.population = population;
		this.function = function;
		
		this.selection = new SelectionOperator(function);
		this.reduction = new ReductionOperator(function);
		
		this.iterationCount = iterationCount;
		this.maxDuration = Duration.between(LocalTime.MIN, LocalTime.parse(maxTime));
		
		this.crossoverChance = crossoverChance;
		this.inversionChance = inversionChance;
		this.mutationChance = mutationChance;
		this.transpositionChance = transpositionChance;
		this.duplicationAndDeletionChance = duplicationAndDeletionChance;
	}
	
	public Population startHolland()
	{	
		Population p = new Population(population.getChromosomes());		
		rand = new Random();
		
		sendLog.emit("Алгоритм Холланда");
		sendLog.emit(String.format("Вероятность кроссинговера: %1$f, инверсии: %2$f, мутации: %3$f", crossoverChance, inversionChance, mutationChance));
		
		sendLog.emit("Начальная популяция");
		logPopulationWithFunctionValue(p);
		
		for (int i=0; i<iterationCount; i++)
		{
			sendLog.emit("Итерация №" + (i+1));		

			Population newGeneration = performSelection(p, 2);
			
			newGeneration = performCrossover(newGeneration, 0, 1);			
			Chromosome newChromosome = chooseChildrenAfterCrossover(newGeneration);				
			newChromosome = performInversion(newChromosome);			
			newChromosome = performMutation(newChromosome);
						
			p.getChromosomes().add(newChromosome);
			
			p = performReduction(p, 1);
			
			sendLog.emit("Результат");
			logPopulationWithFunctionValue(p);
		}
		
		return p;
	}
	
	private void logPopulationWithFunctionValue(Population p)
	{
		sendPopulation.emit(p);
		Double maxValue = Double.NEGATIVE_INFINITY;		
		Double averageValue = 0.0;
		
		for (Chromosome c: p.getChromosomes())
		{
			double value = function.calculate(c);
			averageValue += value;
			if (value > maxValue) maxValue = value;
		}		
		averageValue /= p.getLength();
		sendLog.emit("Наибольшая ЦФ: " + maxValue.toString());
		sendLog.emit("Средняя ЦФ: " + averageValue.toString());
	}
	
	private Population performCrossover(Population p, int firstIndex, int secondIndex)
	{
		if (NumberGenerator.rollWithChance(rand, crossoverChance))
		{
			sendLog.emit("Кроссинговер");
			p = CrossoverOperator.orderedCrossover(p.getChromosomes().get(firstIndex), p.getChromosomes().get(secondIndex));
			sendPopulation.emit(p);
		}
		
		return p;
	}	
	
	private Chromosome chooseChildrenAfterCrossover(Population p)
	{
		sendLog.emit("Выбор хромосомы для включения");
		
		Chromosome c;
		if (NumberGenerator.rollWithChance(rand, 0.5))
			c = p.getChromosomes().get(0);
		else c = p.getChromosomes().get(1);			
		
		sendChromosome.emit(c);
		
		return c;
	}
	
	private Population performSelection(Population p, int count)
	{
		sendLog.emit("Селекция");
		Population newGeneration = selection.tournamentSelection(p, count);			
		sendPopulation.emit(newGeneration);
		
		return newGeneration;
	}
	
	private Chromosome performInversion(Chromosome c)
	{
		if (NumberGenerator.rollWithChance(rand, inversionChance))
		{
			sendLog.emit("Инверсия");
			c = MutationOperator.inversionMutation(c, 1);
			sendChromosome.emit(c);
		}
		
		return c;
	}
	
	private Chromosome performMutation(Chromosome c)
	{
		if (NumberGenerator.rollWithChance(rand, mutationChance))
		{
			sendLog.emit("Мутация");
			c = MutationOperator.pointMutation(c, 1);
			sendChromosome.emit(c);
		}
		
		return c;
	}
	
	private Population performReduction(Population p, int count)
	{
		sendLog.emit("Редукция");
		p = reduction.outstandingReduction(p, count);
		
		return p;
	}	
	
	public Population startGoldberg()
	{	
		Population p = new Population(population.getChromosomes());		
		rand = new Random();
		
		sendLog.emit("Алгоритм Гольдберга");
		sendLog.emit(String.format("Вероятность кроссинговера: %1$f, мутации: %2$f", crossoverChance, mutationChance));
		
		sendLog.emit("Начальная популяция");
		logPopulationWithFunctionValue(p);
		
		for (int i=0; i<iterationCount; i++)
		{
			sendLog.emit("Итерация №" + (i+1));		

			Population newGeneration = performSelection(p, 2);
			
			newGeneration = performCrossover(newGeneration, 0, 1);			
			Chromosome newChromosome = chooseChildrenAfterCrossover(newGeneration);				
			newChromosome = performMutation(newChromosome);
						
			p.getChromosomes().add(newChromosome);
			
			p = performReduction(p, 1);
			
			sendLog.emit("Результат");
			logPopulationWithFunctionValue(p);
		}
		
		return p;
	}
	
	public Population startDavis()
	{
		Population p = new Population(population.getChromosomes());		
		rand = new Random();
		
		sendLog.emit("Алгоритм Дэвиса");
		sendLog.emit(String.format("Вероятность кроссинговера: %1$f, мутации: %2$f", crossoverChance, mutationChance));
		
		sendLog.emit("Начальная популяция");
		logPopulationWithFunctionValue(p);
		
		LocalTime startTime = LocalTime.now();
		
		int i = 0;
		
		while (true)
		{
			if (isTimeout(startTime))
			{
				sendLog.emit("Время вышло");
				logPopulationWithFunctionValue(p);
				return p;
			}
			
			int count = NumberGenerator.getRandomNumberInRange(rand, 2, p.getChromosomes().size() - 1);
			if ((count % 2) != 0) 
				count -=1;
			
			sendLog.emit("Выбрано " + count);
			
			sendLog.emit("Итерация №" + (i+1));		

			Population selectedPopulation = performSelection(p, count);
			Population newGeneration = new Population();
			
			for (int j=0; j<count; j+=2)
				newGeneration.getChromosomes().addAll((performCrossover(selectedPopulation, j, j+1)).getChromosomes());
			
			for (Chromosome c: newGeneration.getChromosomes())
				c = performMutation(c);
			
			p.getChromosomes().addAll(newGeneration.getChromosomes());
			
			p = performReduction(p, count);
			
			sendLog.emit("Результат");
			logPopulationWithFunctionValue(p);
			
			i++;
		}
	}
	
	public Population startModded()
	{
		Population p = new Population(population.getChromosomes());		
		rand = new Random();
		
		sendLog.emit("Модифицированный алгоритм");
		sendLog.emit(String.format("Вероятность кроссинговера: %1$f, инверсии: %2$f, мутации: %3$f, транспозиции: %4$f, дупликации/делеции: %5$f", 
				crossoverChance, inversionChance, mutationChance, transpositionChance, duplicationAndDeletionChance));
		
		sendLog.emit("Начальная популяция");
		logPopulationWithFunctionValue(p);
		
		for (int i=0; i<iterationCount; i++)
		{
			sendLog.emit("Итерация №" + (i+1));		

			Population newGeneration = performSelection(p, 2);
			
			newGeneration = performCrossover(newGeneration, 0, 1);			
			Chromosome newChromosome = chooseChildrenAfterCrossover(newGeneration);				
			newChromosome = performInversion(newChromosome);			
			newChromosome = performMutation(newChromosome);
			newChromosome = performTransposition(newChromosome);
			newChromosome = performDuplicationAndDeletion(newChromosome);
						
			p.getChromosomes().add(newChromosome);
			
			p = performReduction(p, 1);
			
			sendLog.emit("Результат");
			logPopulationWithFunctionValue(p);
		}
		
		return p;
	}
	
	private boolean isTimeout(LocalTime startTime)
	{
		Duration currentDuration = Duration.between(startTime, LocalTime.now());
		if (currentDuration.compareTo(maxDuration) > 0)
			return true;
		
		return false;
	}

	private Chromosome performTransposition(Chromosome c)
	{
		if (NumberGenerator.rollWithChance(rand, transpositionChance))
		{
			sendLog.emit("Транспозиция");
			c = MutationOperator.transpositionMutation(c);
			sendChromosome.emit(c);
		}
		
		return c;
	}
	
	private Chromosome performDuplicationAndDeletion(Chromosome c)
	{
		if (NumberGenerator.rollWithChance(rand, duplicationAndDeletionChance))
		{
			sendLog.emit("Дупликация");
			c = MutationOperator.duplicationMutation(c, rand.nextInt(c.getLength()), 2);
			sendChromosome.emit(c);
			
			sendLog.emit("Делеция");
			c = MutationOperator.deletionMutation(c, rand.nextInt(c.getLength()-1), 1);
			sendChromosome.emit(c);
		}
		
		return c;
	}

}
